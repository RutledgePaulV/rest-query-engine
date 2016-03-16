package com.github.rutledgepaulv.rqe.pipes;

import com.github.rutledgepaulv.qbuilders.visitors.ElasticsearchVisitor;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import com.github.rutledgepaulv.rqe.argconverters.ArgConverter;
import com.github.rutledgepaulv.rqe.contexts.ArgConversionContext;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

/**
 * {@see https://github.com/RutledgePaulV/rest-query-engine/issues/3}
 */
public class DemoCustomConversionPipeline {


    public static class Root {
        ChemicalCompound chemicalCompound;
    }

    public static class ChemicalCompound {
        public List<MetaData> metaData;
    }


    public static class Score {}

    public static class MetaData {
        private String category;
        private boolean computed;
        private boolean deleted;
        private boolean hidden;
        private String name;
        private Score score;
        private String unit;
        private String url;
        private Object value;
        private List<MetaData> metaData;
    }


    private QueryConversionPipeline pipeline = QueryConversionPipeline.builder()
            .useNonDefaultArgumentConversionPipe(DefaultArgumentConversionPipe.builder()
                    .addCustomArgumentConverter(new MetaDataValuePropertyConversionPipe())
                    .build())
            .build();




    private static class MetaDataValuePropertyConversionPipe implements ArgConverter {

        @Override
        public boolean supports(ArgConversionContext context) {
            return "value".equals(context.getPropertyPath().asKey()) &&
                     context.getEntityType().equals(MetaData.class) &&
                    !context.getValues().isEmpty();
        }

        @Override
        public List<?> apply(ArgConversionContext argConversionContext) {
            String value = argConversionContext.getValues().stream().findFirst().get();

            if (Boolean.TRUE.toString().equals(value)) {
                return singletonList(true);
            } else if (Boolean.FALSE.toString().equals(value)) {
                return singletonList(false);
            }

            Optional<Number> number = tryParseNumber(value);
            if (number.isPresent()) {
                return singletonList(number.get());
            }

            return singletonList(value);
        }


        private static Optional<Number> tryParseNumber(String value) {
            try {
                return Optional.of(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                try {
                    return Optional.of(Double.parseDouble(value));
                } catch (NumberFormatException e2) {
                    return Optional.empty();
                }
            }
        }


    }




    @Test
    public void test() {

        Criteria criteria;
        String rsql;


        rsql = "chemicalCompound.metaData=q='name==\"total exact mass\" and value=gt=411.31 and value=lt=411.4'";
        criteria = pipeline.apply(rsql, Root.class).query(new MongoVisitor());
        assertEquals("{ \"chemicalCompound.metaData\" : { \"$elemMatch\" : { \"$and\" :" +
                " [ { \"name\" : \"total exact mass\"} , { \"value\" : { \"$gt\" : 411.31}} ," +
                " { \"value\" : { \"$lt\" : 411.4}}]}}}", criteria.getCriteriaObject().toString());


        rsql = "chemicalCompound.metaData=q='name==\"total exact mass\" and value=gt=1 and value=lt=5'";
        criteria = pipeline.apply(rsql, Root.class).query(new MongoVisitor());
        assertEquals("{ \"chemicalCompound.metaData\" : { \"$elemMatch\" : { \"$and\" :" +
                " [ { \"name\" : \"total exact mass\"} , { \"value\" : { \"$gt\" : 1}} ," +
                " { \"value\" : { \"$lt\" : 5}}]}}}", criteria.getCriteriaObject().toString());


        rsql = "chemicalCompound.metaData=q='name==\"total exact mass\" and value==true'";
        criteria = pipeline.apply(rsql, Root.class).query(new MongoVisitor());
        assertEquals("{ \"chemicalCompound.metaData\" : { \"$elemMatch\" : { \"$and\" :" +
                " [ { \"name\" : \"total exact mass\"} , { \"value\" : true}]}}}",
                criteria.getCriteriaObject().toString());


        rsql = "chemicalCompound.metaData=q='name==\"total exact mass\" and value==false'";
        criteria = pipeline.apply(rsql, Root.class).query(new MongoVisitor());
        assertEquals("{ \"chemicalCompound.metaData\" : { \"$elemMatch\" : { \"$and\" :" +
                " [ { \"name\" : \"total exact mass\"} , { \"value\" : false}]}}}",
                criteria.getCriteriaObject().toString());


    }


    @Test
    public void nestedQueryAgainstElasticsearchMaintainsFieldPathNameOnNestedElements() {

        String rsql = "metaData=q='name==\"license\" and value==\"CC BY-SA\"'";
        QueryBuilder builder = pipeline.apply(rsql, ChemicalCompound.class).query(new ElasticsearchVisitor(), new ElasticsearchVisitor.Context());


        assertEquals("{\n" +
                "  \"nested\" : {\n" +
                "    \"query\" : {\n" +
                "      \"bool\" : {\n" +
                "        \"must\" : [ {\n" +
                "          \"term\" : {\n" +
                "            \"metaData.name\" : \"license\"\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"term\" : {\n" +
                "            \"metaData.value\" : \"CC BY-SA\"\n" +
                "          }\n" +
                "        } ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"path\" : \"metaData\"\n" +
                "  }\n" +
                "}",builder.toString());
    }

    @Test
    public void multipleDepthsOfNested() {

        String rsql = "metaData=q='name==\"license\" and value==\"CC BY-SA\" and metaData=q=\"name==notLicense\"'";

        QueryBuilder builder = pipeline.apply(rsql, ChemicalCompound.class).query(new ElasticsearchVisitor(), new ElasticsearchVisitor.Context());


        assertEquals("{\n" +
                "  \"nested\" : {\n" +
                "    \"query\" : {\n" +
                "      \"bool\" : {\n" +
                "        \"must\" : [ {\n" +
                "          \"term\" : {\n" +
                "            \"metaData.name\" : \"license\"\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"term\" : {\n" +
                "            \"metaData.value\" : \"CC BY-SA\"\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"nested\" : {\n" +
                "            \"query\" : {\n" +
                "              \"term\" : {\n" +
                "                \"metaData.metaData.name\" : \"notLicense\"\n" +
                "              }\n" +
                "            },\n" +
                "            \"path\" : \"metaData.metaData\"\n" +
                "          }\n" +
                "        } ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"path\" : \"metaData\"\n" +
                "  }\n" +
                "}",builder.toString());
    }

}

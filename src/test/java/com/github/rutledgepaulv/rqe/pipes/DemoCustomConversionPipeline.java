package com.github.rutledgepaulv.rqe.pipes;

import com.github.rutledgepaulv.qbuilders.visitors.ElasticsearchVisitor;
import com.github.rutledgepaulv.qbuilders.visitors.MongoVisitor;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * {@see https://github.com/RutledgePaulV/rest-query-engine/issues/3}
 */
public class DemoCustomConversionPipeline {


    public static class Spectra {
        private List<ChemicalCompound> compounds = new LinkedList<>();
    }

    public static class ChemicalCompound {
        private String name;
        private List<MetaData> metaData = new LinkedList<>();
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
        private List<MetaData> metaData = new LinkedList<>();
    }


    private QueryConversionPipeline pipeline = QueryConversionPipeline.builder().build();


    @Test
    public void doubleNestedQuery() {

        String rsql = "compounds=q='name==\"Test\";metaData=q=\"(category==Acid;hidden==false)\"'";

        Criteria criteria = pipeline.apply(rsql, Spectra.class).query(new MongoVisitor());
        assertEquals("{ \"compounds\" : { \"$elemMatch\" : { \"$and\" : " +
                "[ { \"name\" : \"Test\"} , { \"metaData\" : { \"$elemMatch\" :" +
                " { \"$and\" : [ { \"category\" : \"Acid\"} ," +
                " { \"hidden\" : false}]}}}]}}}", criteria.getCriteriaObject().toString());


        QueryBuilder builder = pipeline.apply(rsql, Spectra.class)
                .query(new ElasticsearchVisitor(), new ElasticsearchVisitor.Context());

        assertEquals("{\n" +
                "  \"nested\" : {\n" +
                "    \"query\" : {\n" +
                "      \"bool\" : {\n" +
                "        \"must\" : [ {\n" +
                "          \"term\" : {\n" +
                "            \"compounds.name\" : \"Test\"\n" +
                "          }\n" +
                "        }, {\n" +
                "          \"nested\" : {\n" +
                "            \"query\" : {\n" +
                "              \"bool\" : {\n" +
                "                \"must\" : [ {\n" +
                "                  \"term\" : {\n" +
                "                    \"compounds.metaData.category\" : \"Acid\"\n" +
                "                  }\n" +
                "                }, {\n" +
                "                  \"term\" : {\n" +
                "                    \"compounds.metaData.hidden\" : false\n" +
                "                  }\n" +
                "                } ]\n" +
                "              }\n" +
                "            },\n" +
                "            \"path\" : \"compounds.metaData\"\n" +
                "          }\n" +
                "        } ]\n" +
                "      }\n" +
                "    },\n" +
                "    \"path\" : \"compounds\"\n" +
                "  }\n" +
                "}", builder.toString());

    }


    @Test
    public void testNestedQueries() {

        Criteria criteria;
        String rsql;


        rsql = "compounds.metaData=q='name==\"total exact mass\" and value=gt=411.31 and value=lt=411.4'";
        criteria = pipeline.apply(rsql, Spectra.class).query(new MongoVisitor());
        assertEquals("{ \"compounds.metaData\" : { \"$elemMatch\" : { \"$and\" :" +
                " [ { \"name\" : \"total exact mass\"} , { \"value\" : { \"$gt\" : 411.31}} ," +
                " { \"value\" : { \"$lt\" : 411.4}}]}}}", criteria.getCriteriaObject().toString());


        rsql = "compounds.metaData=q='name==\"total exact mass\" and value=gt=1 and value=lt=5'";
        criteria = pipeline.apply(rsql, Spectra.class).query(new MongoVisitor());
        assertEquals("{ \"compounds.metaData\" : { \"$elemMatch\" : { \"$and\" :" +
                " [ { \"name\" : \"total exact mass\"} , { \"value\" : { \"$gt\" : 1}} ," +
                " { \"value\" : { \"$lt\" : 5}}]}}}", criteria.getCriteriaObject().toString());


        rsql = "compounds.metaData=q='name==\"total exact mass\" and value==true'";
        criteria = pipeline.apply(rsql, Spectra.class).query(new MongoVisitor());
        assertEquals("{ \"compounds.metaData\" : { \"$elemMatch\" : { \"$and\" :" +
                " [ { \"name\" : \"total exact mass\"} , { \"value\" : true}]}}}",
                criteria.getCriteriaObject().toString());


        rsql = "compounds.metaData=q='name==\"total exact mass\" and value==false'";
        criteria = pipeline.apply(rsql, Spectra.class).query(new MongoVisitor());
        assertEquals("{ \"compounds.metaData\" : { \"$elemMatch\" : { \"$and\" :" +
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

        QueryBuilder builder = pipeline.apply(rsql, ChemicalCompound.class)
                                       .query(new ElasticsearchVisitor(), new ElasticsearchVisitor.Context());


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

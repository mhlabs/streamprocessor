package org.streamprocessor.core.transforms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.beam.sdk.coders.Coder.Context;
//import org.apache.beam.sdk.io.gcp.bigquery.BigQueryUtils;
import org.streamprocessor.core.utils.BigQueryUtils;
import org.apache.beam.sdk.io.gcp.bigquery.TableRowJsonCoder;
//import org.apache.beam.sdk.io.gcp.bigquery.BigQueryUtils.SchemaConversionOptions;
import org.apache.beam.sdk.schemas.Schema;
import org.apache.beam.sdk.util.RowJson.RowJsonDeserializer;
import org.apache.beam.sdk.util.RowJsonUtils;
import org.apache.beam.sdk.values.Row;
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.collect.ImmutableMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.streamprocessor.core.utils.CacheLoaderUtils;

import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;


// import com.google.cloud.bigquery.BigQuery;

public class GetSchemaTests {

    private static final Logger LOG = LoggerFactory.getLogger(GetSchemaTests.class);

    public static TableRow convertJsonToTableRow(String json) {
      TableRow row;
      // Parse the JSON into a {@link TableRow} object.
      try (InputStream inputStream =
          new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))) {
          row = TableRowJsonCoder.of().decode(inputStream, Context.OUTER);
        
  
      } catch (IOException e) {
        throw new RuntimeException("Failed to serialize json to table row: " + json, e);
      }
  
      return row;
    }
      
    public static void main(String[] args) {

        // Schema schema =
        // new Schema.Builder()
        //     .addField(Schema.Field.of("firstname", Schema.FieldType.STRING))
        //     //.addField(Schema.Field.of("timestamp", Schema.FieldType.DATETIME))
        //     //.addNullableField("products", Schema.FieldType.map(Schema.FieldType.STRING, Schema.FieldType.STRING))
        //     //.addMapField("products", Schema.FieldType.STRING, Schema.FieldType.row(new Schema.Builder().addStringField("foo").addStringField("bar").build()))
        //     .addArrayField("products", Schema.FieldType.row(new Schema.Builder().addStringField("foo").build()))
        //     //.addRowField("struc", new Schema.Builder()
        //     //.addField(Schema.Field.of("timestamp", Schema.FieldType.DATETIME)).build())
        //     .build();

        // LOG.info(schema.toString());
        String arrayString =
                    String.format(
                            "//bigquery.googleapis.com/projects/%s/datasets/%s/tables/%s",
                            "mathem-ml-datahem-test", "airbyte_test", "map_row");
        
        Schema schema = CacheLoaderUtils.getSchema(arrayString);
        LOG.info("map row schema: " + schema.toString());
        TableSchema ts = BigQueryUtils.toTableSchema(schema);
        LOG.info("table schema: " + ts.toString());

        Schema brs = BigQueryUtils.fromTableSchema(ts, BigQueryUtils.SchemaConversionOptions.builder().setInferMaps(true).build());
        LOG.info(brs.toString());
        
        LOG.info("------------------");

        String arrayRow =
                    String.format(
                            "//bigquery.googleapis.com/projects/%s/datasets/%s/tables/%s",
                            "mathem-ml-datahem-test", "airbyte_test", "array_row_test");
        
        Schema schemaRow = CacheLoaderUtils.getSchema(arrayRow);
        LOG.info("row schema: " + schemaRow.toString());
        TableSchema tsRow = BigQueryUtils.toTableSchema(schemaRow);
        LOG.info("table schema: " + tsRow.toString());

        Schema brsRow = BigQueryUtils.fromTableSchema(ts, BigQueryUtils.SchemaConversionOptions.builder().setInferMaps(true).build());
        LOG.info(brsRow.toString());
        // JSONObject json =
        //   new JSONObject()
        //       .put("firstname", "Joe")
        //       .put("products", new JSONArray().put(new JSONObject().put("foo", "bar")));
        //       //.put("products", new JSONObject().put("foo", "bar").put("hello", "world"))
        //       //.put("struc", new JSONObject().put("lastname", "doe"));
        // LOG.info(json.toString());


        //  Schema MAP_MAP_TYPE = Schema.builder().addMapField("map", Schema.FieldType.STRING, Schema.FieldType.DOUBLE).build();
        //  Row MAP_ROW = Row.withSchema(MAP_MAP_TYPE).addValues(ImmutableMap.of("test", 123.456, "test2", 12.345)).build();
        //  TableRow row = BigQueryUtils.toTableRow().apply(MAP_ROW);
        //  LOG.info(row.toString());

        // Row br = BigQueryUtils.toBeamRow(MAP_MAP_TYPE, row);
        // LOG.info(br.toString());

        // TableRow tr = convertJsonToTableRow(json.toString());
        // LOG.info("table row " + tr.toString());

        // LOG.info("schema: " + schema.toString());

        // Row br = BigQueryUtils.toBeamRow(schema, tr);
        // LOG.info("beam row" + br.toString());
        

        // Row row = RowJsonUtils.jsonToRow(
        // RowJsonUtils.newObjectMapperWith(
        //   RowJsonDeserializer
        //     .forSchema(schema)
        //     .withNullBehavior(RowJsonDeserializer.NullBehavior.ACCEPT_MISSING_OR_NULL)), 
        //   json.toString());
      
        //   LOG.info(row.toString());

          
          
    }
}
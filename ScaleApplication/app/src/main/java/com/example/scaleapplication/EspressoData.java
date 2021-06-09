package com.example.scaleapplication;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.io.ByteArrayOutputStream;

public class EspressoData {
    private String coffeeGrams;
    private String coffeeRatio;
    private int coffeeAge;
    private float waterTemperature;
    private float espressoPressure;
    private String[] coffeeType;
    private  JSONArray jsonObject;

    public boolean fileExist(Context context, String fileName){
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }
    public void saveJsonOnStorage(Context context){
        //File root = android.os.Environment.getExternalStorageDirectory();
        String path = context.getFilesDir().getAbsolutePath()+"/espresso_data";
        File file = new File(path);
        Log.e("EspressoData","\nExternal file system root: "+path);
        //File dir = new File (root.getAbsolutePath() + "/JSON");
        if(!file.exists()){
            String jsonContent = "{\n" +
                    "  \"EspressoData\": {\n" +
                    "    \"coffeeGrams\": \"154\",\n" +
                    "    \"coffeeRatio\": \"2\",\n" +
                    "    \"coffeeAge\": \"14\",\n" +
                    "    \"waterTemperature\": \"85\",\n" +
                    "    \"espressoPressure\": \"9\",\n" +
                    "    \"coffeeType\": [\n" +
                    "      \"blend\",\n" +
                    "      \"single origin\"\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}";
            try (FileOutputStream fos = new FileOutputStream(file)) {
                PrintWriter printWriter = new PrintWriter(fos);
                printWriter.print(jsonContent);
                printWriter.close();
            }
            catch (Exception e){
                Log.e("EspressoData",e.toString());
            }
        }
    }
   public void setJsonValue(Context context, String jsonAttribute, String jsonValue){

        try {
            //AssetManager assetManager = context.getAssets();
            //InputStream inputStream = assetManager.open("espresso_data.json");
            String path = context.getFilesDir().getAbsolutePath()+"/espresso_data";
            File file = new File(path);
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(file);
            Object jsonObject = jsonParser.parse(reader);
            JSONObject jsonData = (JSONObject) jsonObject;
            JSONObject espressoData = (JSONObject) jsonData.get("EspressoData");
            espressoData.put(jsonAttribute,jsonValue);
                try (FileWriter fileWritter = new FileWriter(file)) {
                    System.out.println(fileWritter.toString());
                    fileWritter.write(jsonData.toJSONString());
                    fileWritter.flush();
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }
    public String getJsonValue(Context context, String jsonAttribute){
        try {
            //AssetManager assetManager = context.getAssets();
            //InputStream inputStream = assetManager.open("espresso_data.json");
            String path = context.getFilesDir().getAbsolutePath()+"/espresso_data";
            File file = new File(path);
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(file);
            JSONArray jsonArray = new JSONArray();
            Object jsonObject = jsonParser.parse(reader);
            jsonArray.add(jsonObject);
            for(Object object : jsonArray) {
                JSONObject jsonData = (JSONObject) object;
                JSONObject espressoData = (JSONObject) jsonData.get("EspressoData");
                String value =(String) espressoData.get(jsonAttribute);
                reader.close();
                return value;
            }
        }
        catch (Exception e){
            e.getStackTrace();
        }
        return "";
    }
    public EspressoData(Context context){

    //    this.coffeeGrams = getJsonValue(context,"coffeeGrams");
    //    this.coffeeRatio = getJsonValue(context,"coffeeRatio");
//        this.coffeeAge = Integer.parseInt( getJsonValue(context,"coffeeAge"));
  //      this.waterTemperature = Float.parseFloat(getJsonValue(context,"waterTemperature"));
    //    this.coffeeType=  getJsonValue(context,"coffeeType").split(",");
    }

    public String getCoffeeGrams() {
        return coffeeGrams;
    }

    public void setCoffeeGrams(String coffeeGrams) {
        for (Object object : jsonObject) {
            JSONObject espressoData = (JSONObject) object;
            espressoData.put("coffeeGrams", coffeeGrams);
        }
    }

    public String getCoffeeRatio() {
        return coffeeRatio;
    }

    public void setCoffeeRatio(String coffeeRatio) {
        for (Object object : jsonObject) {
            JSONObject espressoData = (JSONObject) object;
            espressoData.put("coffeeRatio", coffeeRatio);
        }
    }

    public int getCoffeeAge() {
        return coffeeAge;
    }

    public void setCoffeeAge(int coffeeAge) {
        for (Object object : jsonObject) {
            JSONObject espressoData = (JSONObject) object;
            espressoData.put("coffeeAge", coffeeAge);
        }
    }

    public float getWaterTemperature() {
        return waterTemperature;
    }

    public void setWaterTemperature(float waterTemperature) {
        for (Object object : jsonObject) {
            JSONObject espressoData = (JSONObject) object;
            espressoData.put("waterTemperature", waterTemperature);
        }
    }

    public float getEspressoPressure() {
        return espressoPressure;
    }

    public void setEspressoPressure(float espressoPressure) {
        for (Object object : jsonObject) {
            JSONObject espressoData = (JSONObject) object;
            espressoData.put("espressoPressure", espressoPressure);
        }
    }

    public String[] getCoffeeType() {
        return coffeeType;
    }

    public void setCoffeeType(String[] coffeeType) {
        for (Object object : jsonObject) {
            JSONObject espressoData = (JSONObject) object;
            espressoData.put("coffeeType", coffeeType);
        }
    }

}

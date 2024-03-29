package be.howest.nmct.receptenapp.data.UnitData;

import android.content.ContentValues;
import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.howest.nmct.receptenapp.contentprovider.ReceptenAppContentProvider;
import be.howest.nmct.receptenapp.data.helpers.onlineData;

/**
 * Created by Toine on 5/11/2014.
 */
public class Unit {
    private int ID;
    private String Name;
    private String Abbreviation;

    public Unit() {
    }

    public Unit(int ID, String name, String abbreviation) {
        this.ID = ID;
        this.Name = name;
        this.Abbreviation = abbreviation;
    }

    public static ArrayList<Unit> getAllUnits() {
        ArrayList<Unit> list = new ArrayList<Unit>();
        JSONArray units = onlineData.selectAllData("ap_unit");
        if (units != null) {
            for (int i = 0; i < units.length(); i++) {
                try {
                    JSONObject c = units.getJSONObject(i);
                    int id = c.getInt("ID");
                    String name = c.getString("Name");
                    String abbr = c.getString("Abbreviation");
                    Unit unit = new Unit(id, name, abbr);
                    list.add(unit);
                } catch (Exception e) {
                }
            }
            return list;
        } else {
            return null;
        }
    }
    public static Unit getUnitById(int id) {
        Unit u = new Unit();
        JSONArray unit = onlineData.selectDataById("ap_unit", id);
        if (unit != null && unit.length() == 1) {
            try {
                JSONObject c = unit.getJSONObject(0);

                int ID = c.getInt("ID");
                String Abbreviation = c.getString("Abbreviation");
                String Name = c.getString("Name");
                Unit newUnit = new Unit(ID, Name, Abbreviation);
                u = newUnit;
                //return newUnit;
            } catch (Exception e) {
            }
            //return u;
            } else {
            return null;
        }
        return u;
    }
    public static void createUnit(String _name, String _abbr){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_unit"));
        params.add(new BasicNameValuePair("Name",_name));
        params.add(new BasicNameValuePair("Abbreviation", _abbr));
        onlineData.create(params);
    }
    public static void deleteUnit(int _id){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tableName", "ap_unit"));
        params.add(new BasicNameValuePair("id", ""+_id));
        onlineData.delete(params);
    }

    public static Boolean LoadAllUnitsCURSOR(Context context){
        context.getContentResolver().delete(ReceptenAppContentProvider.CONTENT_URI_UNIT,null,null);

        JSONArray ingredients = onlineData.selectAllData("ap_unit");
        if(ingredients != null) {
            for (int i = 0; i < ingredients.length(); i++) {
                try {
                    JSONObject c = ingredients.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.put(UnitTable.COLUMN_ID, c.getInt("ID"));
                    values.put(UnitTable.COLUMN_NAME, c.getString("Name"));
                    values.put(UnitTable.COLUMN_ABBREVIATION, c.getString("Abbreviation"));
                    context.getContentResolver().insert(ReceptenAppContentProvider.CONTENT_URI_UNIT, values);
                } catch (Exception e) {
                }
            }
            return true;
        }else{
            return false;
        }
    }
}

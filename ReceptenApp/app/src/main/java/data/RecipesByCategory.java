package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tijs1 on 3-12-2014.
 */
public class RecipesByCategory {
    //region properties
    private int ID;
    private int CategoryId;
    private Category Category;
    private ArrayList<Recept> Recipes;

    //endregion
    //region constructors
    public RecipesByCategory() {
    }

    public RecipesByCategory(int id, int categoryId, Category cat, ArrayList<Recept> recipes) {
        this.ID = id;
        this.CategoryId = categoryId;
        this.Category = cat;
        this.Recipes = recipes;
    }
    //endregion

    public static ArrayList<RecipesByCategory> getAllRecipesByCategory() {
        ArrayList<RecipesByCategory> list = new ArrayList<RecipesByCategory>();
        JSONArray recByCat = data.helpers.onlineData.selectAllData("ap_recipes_by_category");
        if (recByCat != null) {
            for (int i = 0; i < recByCat.length(); i++) {
                try {
                    JSONObject c = recByCat.getJSONObject(i);
                    int id = c.getInt("ID");
                    int catId = c.getInt("CategoryId");
                    Category cat = makeCategory(catId);
                    ArrayList<Recept> recs = makeRecipes(c.getString("RecipeIds"));
                    RecipesByCategory newRecByCat = new RecipesByCategory(id, catId, cat, recs);
                    list.add(newRecByCat);
                } catch (Exception e) {
                }
            }
        } else {
            return null;
        }
        return list;
    }

    public static RecipesByCategory getRecipeByCatId(int catId) {
        RecipesByCategory recByCat = new RecipesByCategory();
        JSONArray recipesByCategory = data.helpers.onlineData.selectRecipesByCatId("ap_recipes_by_category", catId);
        if (recipesByCategory != null && recipesByCategory.length() == 1) {
            try {
                JSONObject c = recipesByCategory.getJSONObject(0);
                int id = c.getInt("ID");
                int _catId = c.getInt("CategoryId");
                Category cat = makeCategory(catId);
                ArrayList<Recept> recs = makeRecipes(c.getString("RecipeIds"));
                RecipesByCategory newRecByCat = new RecipesByCategory(id, _catId, cat, recs);
                recByCat = newRecByCat;
            } catch (Exception e) {
            }
        } else {
            return null;
        }
        return recByCat;
    }

    private static Category makeCategory(int catId) {
        Category cat = data.Category.getCategoryById(catId);
        return cat;
    }

    private static ArrayList<Recept> makeRecipes(String recipeIds) {
        ArrayList<Recept> lijst = new ArrayList<Recept>();
        String[] sDelen = recipeIds.split(";");
        for(int i = 0; i < sDelen.length; i++){
            Recept rec = Recept.getRecipeById(Integer.parseInt(sDelen[i]));
            lijst.add(rec);
        }
        return lijst;
    }


}
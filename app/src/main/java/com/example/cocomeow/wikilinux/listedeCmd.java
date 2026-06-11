package com.example.cocomeow.wikilinux;

import android.content.Context; // Nécessaire pour accéder aux assets d'Android
import com.google.gson.Gson;    // Pour parser le JSON automatiquement
import com.google.gson.reflect.TypeToken; // Pour que Gson comprenne l'ArrayList

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
public class listedeCmd {
    private String texte;
    private String tag;

    public listedeCmd(String texte,String tag) {

        this.texte = texte;
        this.tag = tag;
    }

    public String getTexte() {
        return this.texte;
    }

    public String getTag()
    {
        return this.tag;
    }
    public void setTexte(String nouveauTexte) {
        this.texte = nouveauTexte;
    }

    public static ArrayList<listedeCmd> genererListe(Context context) {

        String jsonString;
        ArrayList<listedeCmd> liste;

        try {
            // 1. Ouvrir le fichier commands.json depuis le dossier assets
            InputStream is = context.getAssets().open("commands.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convertir le flux de données en String standard
            jsonString = new String(buffer, StandardCharsets.UTF_8);

            // 2. Configuration de GSON pour remplir automatiquement l'ArrayList
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<listedeCmd>>(){}.getType();

            // Gson associe directement la clé "texte" du JSON à ta variable 'texte'
            // et la clé "tag" à ta variable 'tag'
            liste = gson.fromJson(jsonString, listType);

        } catch (IOException e) {
            e.printStackTrace();
            // En cas de problème de lecture, on renvoie une liste vide pour éviter un crash complet
            liste = new ArrayList<>();
        }
        return liste;
    }
}

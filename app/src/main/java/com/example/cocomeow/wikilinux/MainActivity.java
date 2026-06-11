package com.example.cocomeow.wikilinux;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Spinner choix;
    private TextView cmd;

    private Button btnSuivant;

    private Button btnPrecedant;

    private SearchView recherche;

    private ArrayList<listedeCmd> listeComplete = new ArrayList<>(); // Contient TOUTES les commandes de l'app
    private ArrayList<listedeCmd> listeDeCmd = new ArrayList<>();    // Contient uniquement les commandes FILTRÉES
    private int indexActuel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choix = findViewById(R.id.tagSpinner);
        cmd = findViewById(R.id.cmd);
        btnSuivant = findViewById(R.id.btnSuivant);
        btnPrecedant = findViewById(R.id.btnPrecedant);
        recherche = findViewById(R.id.recherche);

        listeComplete = listedeCmd.genererListe(this);
        String[] categorie = {"sudo", "pip","cd","pwd","mkdir","rm","cp","mv","touch","cat","tail","grep",
        "chmod","clear","history","df","free","git"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorie);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choix.setAdapter(adapter);

        choix.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tagSelectionne = categorie[position];
                filtrerLesCmdParTag(tagSelectionne);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        recherche.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrerLesRecherche(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrerLesRecherche(newText);
                return true;
            }
        });

        btnPrecedant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!listeDeCmd.isEmpty())
                {
                    indexActuel--;
                    if(indexActuel < 0)
                    {
                        indexActuel = listeDeCmd.size() -1 ;
                    }

                    afficherCmdActuelle();

                }
            }
        });

        btnSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!listeDeCmd.isEmpty())
                {
                    indexActuel++;
                    if (indexActuel>=listeDeCmd.size())
                    {
                        indexActuel = 0;
                    }


                    afficherCmdActuelle();
                }

            }
        });
    }

    private void filtrerLesCmdParTag(String tagSelectionne) {
        listeDeCmd.clear();
        for(listedeCmd cmd : listeComplete)
        {
            if(cmd.getTag().equalsIgnoreCase(tagSelectionne)){
                listeDeCmd.add(cmd);
            }
        }

        if(!listeDeCmd.isEmpty())
        {
            indexActuel = 0;
            afficherCmdActuelle();
        }
        else {
            cmd.setText("Erreur, aucune commande à l'horizon... Sniff");
        }
    }

    private void filtrerLesRecherche(String content)
    {
        listeDeCmd.clear();

        if(content == null || content.trim().isEmpty()){
            String tagActuel = choix.getSelectedItem().toString();
            filtrerLesCmdParTag(tagActuel);
            return;
        }

        String texteRecherche = content.toLowerCase().trim();

        for(listedeCmd cmdItem : listeComplete) {
            String cmdEnMinuscule = cmdItem.getTexte().toLowerCase();
            if (cmdEnMinuscule.contains(texteRecherche)) {
                listeDeCmd.add(cmdItem);
            }
        }



            if(!listeDeCmd.isEmpty())
            {
                indexActuel = 0;
                afficherCmdActuelle();


            } else {
                cmd.setText("Erreur, aucune commande à l'horizon... Sniff");
            }

    }

    private void afficherCmdActuelle()
    {

        if (listeDeCmd != null && !listeDeCmd.isEmpty()) {

            listedeCmd commande = listeDeCmd.get(indexActuel);
            cmd.setText(commande.getTexte());
        }
    }
}
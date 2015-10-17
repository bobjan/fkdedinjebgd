package com.logotet.fkdedinjebgd;

import com.logotet.dedinjeadmin.AllStatic;
import com.logotet.dedinjeadmin.HttpCatcher;
import com.logotet.dedinjeadmin.model.BazaIgraca;
import com.logotet.dedinjeadmin.model.BazaPozicija;
import com.logotet.dedinjeadmin.model.BazaStadiona;
import com.logotet.dedinjeadmin.model.Fixtures;
import com.logotet.dedinjeadmin.model.Klub;
import com.logotet.dedinjeadmin.model.Tabela;
import com.logotet.dedinjeadmin.model.Utakmica;
import com.logotet.dedinjeadmin.xmlparser.RequestPreparator;

import java.io.IOException;

public class DataFetcher {

    public static void preuzmiRaspored() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Fixtures.getInstance().getRaspored().clear();
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETFIXTURES, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void preuzmiNextMatch() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETLIVEMATCH, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    if(!Utakmica.getInstance().getDatum().greaterThanToday()){
                        catcher = new HttpCatcher(RequestPreparator.GETSASTAV, AllStatic.HTTPHOST, null);
                        catcher.catchData();
                    }
                    catcher = new HttpCatcher(RequestPreparator.SERVERTIME, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    catcher = new HttpCatcher(RequestPreparator.ALLEVENTS, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void preuzmiStadion() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BazaStadiona.getInstance().getTereni().clear();
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETSTADION, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void preuzmiTabelu() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETLIGA, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    Tabela.getInstance().getPlasman().clear();
                    catcher = new HttpCatcher(RequestPreparator.GETTABELA, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void preuzmiRukovodstvo() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Klub.getInstance().getRukovodstvo().clear();
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETRUKOVODSTVO, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public static void preuzmiEkipu() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BazaPozicija.getInstance().getTimposition().clear();
                    HttpCatcher catcher = new HttpCatcher(RequestPreparator.GETPOZICIJA, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    BazaIgraca.getInstance().getSquad().clear();
                    catcher = new HttpCatcher(RequestPreparator.GETEKIPA, AllStatic.HTTPHOST, null);
                    catcher.catchData();
                    if(!Utakmica.getInstance().getDatum().greaterThanToday()){
                        catcher = new HttpCatcher(RequestPreparator.GETSASTAV, AllStatic.HTTPHOST, null);
                        catcher.catchData();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}

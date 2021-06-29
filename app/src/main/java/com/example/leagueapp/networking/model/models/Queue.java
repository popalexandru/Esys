package com.example.leagueapp.networking.model.models;

public enum Queue {
    RANKED_FLEX(440, "Ranked flex"),
    RANKED_SOLO(420, "Ranked solo"),
    ALL_RANDOM(325, "ARSR_5X5"),
    ALL_RANDOM_URF(900, "ARURF"),
    ALL_RANDOM_URF_SNOW(1010, "SNOWURF"),
    ARAM(450, "ARAM"),
    ASCENSION(910, "PROJECT"),
    BLACK_MARKET_BRAWLERS(313, "BILGEWATER_5X5"),
    BLIND_PICK(430, "Normal 5vs5"),
    BLOOD_MOON(600, "ASSASSINATE_5X5"),
    BUTCHERS_BRIDGE(100, "BILGEWATER_ARAM_5X5"),
    CLASH(700, "CLASH"),
    COOP_VS_AI_BEGINNER(840, "BOT_5X5_BEGINNER"),
    COOP_VS_AI_INTERMEDIATE(850, "BOT_5X5_INTERMEDIATE"),
    COOP_VS_AI_INTRO(830, "BOT_5X5_INTRO"),
    COOP_VS_AI_THREES_BEGINNER(820, "BOT_3X3_BEGINNER"),
    COOP_VS_AI_THREES_INTERMEDIATE(800, "BOT_3X3_INTERMEDIATE"),
    COOP_VS_AI_THREES_INTRO(810, "BOT_3X3_INTRO"),
    COOP_VS_AI_URF(83, "BOT_URF_5X5"),
    CUSTOM(0, "CUSTOM"),
    DARKSTAR(610, "DARKSTAR_3X3"),
    DEFINITELY_NOT_DOMINION(317, "DEFINITELY_NOT_DOMINION_5X5"),
    DOOM_BOTS(960, "NIGHTMARE_BOT_5X5"),
    DOOM_BOTS_WITH_VOTING(950, "NIGHTMARE_BOT_5X5_VOTE"),
    HEXAKILL(75, "SR_6X6"),
    INVASION(980, "INVASION_NORMAL"),
    NEMESIS_DRAFT(310, "COUNTER_PICK"),
    ONE_FOR_ALL(1020, "ONEFORALL"),
    ONE_FOR_ALL_MIRROR(78, "ONEFORALL"),
    PORO_KING(920, "KINGPORO"),
    RANKED_THREES(470, "RANKED_FLEX_TT"),
    SHOWDOWN_DUO(73, "FIRSTBLOOD_2X2"),
    SHOWDOWN_SOLO(72, "FIRSTBLOOD_1X1"),
    TFT(1090, "NORMAL_TFT"),
    THREES(460, "NORMAL_3X3_BLIND_PICK"),
    URF(76, "URF");

    public static Queue withID(int id){
        for(Queue queue : Queue.values()){
            if(queue.getId() == id){
                return queue;
            }
        }
        return Queue.RANKED_SOLO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int id;
    private String name;

    Queue(int id, String name){
        this.id = id;
        this.name = name;
    }
}

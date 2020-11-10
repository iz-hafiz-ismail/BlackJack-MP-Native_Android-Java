package com.example.blackjackgame.model;


import com.example.blackjack.R;

public enum Pics {
    ACESPADES(11,R.drawable.ace_of_spades),
    TWOSPADES(2, R.drawable.two_of_spades),
    THREESPADES(3, R.drawable.three_of_spades),
    FOURSPADES(4, R.drawable.four_of_spades),
    FIVESPADES(5, R.drawable.five_of_spades),
    SIXSPADES(6, R.drawable.six_of_spades),
    SEVENSPADES(7, R.drawable.seven_of_spades),
    EIGHTSPADES(8, R.drawable.eight_of_spades),
    NINESPADES(9, R.drawable.nine_of_spades),
    TENSPADES(10, R.drawable.ten_of_spades),
    JACKSPADES( 10, R.drawable.jack_of_spades),
    QUEENSPADES( 10, R.drawable.queen_of_spades),
    KINGSPADES( 10, R.drawable.king_of_spades),
    ACECLUBS(11, R.drawable.ace_of_clubs),
    TWOCLUBS(2, R.drawable.two_of_clubs),
    THREECLUBS(3, R.drawable.three_of_clubs),
    FOURCLUBS(4, R.drawable.four_of_clubs),
    FIVECLUBS(5, R.drawable.five_of_clubs),
    SIXCLUBS(6, R.drawable.six_of_clubs),
    SEVENCLUBS(7, R.drawable.seven_of_clubs),
    EIGHTCLUBS(8, R.drawable.eight_of_clubs),
    NINECLUBS(9, R.drawable.nine_of_clubs),
    TENCLUBS(10, R.drawable.ten_of_clubs),
    JACKCLUBS(10,R.drawable.jack_of_clubs),
    QUEENCLUBS(10, R.drawable.queen_of_clubs),
    KINGCLUBS(10, R.drawable.king_of_clubs),
    ACEDI(11, R.drawable.ace_of_diamonds),
    TWODI(2, R.drawable.two_of_diamonds),
    THREEDI(3, R.drawable.three_of_diamonds),
    FOURDI(4, R.drawable.four_of_diamonds),
    FIVEDI(5, R.drawable.five_of_diamonds),
    SIXDI(6, R.drawable.six_of_diamonds),
    SEVENDI(7, R.drawable.seven_of_diamonds),
    EIGHTDI(8, R.drawable.eight_of_diamonds),
    NINEDI(9, R.drawable.nine_of_diamonds),
    TENDI(10, R.drawable.ten_of_diamonds),
    JACKDI( 10, R.drawable.jack_of_diamonds),
    QUEENDI(10, R.drawable.queen_of_diamonds),
    KINGDI( 10, R.drawable.king_of_diamonds),
    ACEH(11, R.drawable.ace_of_hearts),
    TWOH(2, R.drawable.two_of_hearts),
    THREEH(3, R.drawable.three_of_hearts),
    FOURH(4, R.drawable.four_of_hearts),
    FIVEH(5, R.drawable.five_of_hearts),
    SIXH(6, R.drawable.six_of_hearts),
    SEVENH(7, R.drawable.seven_of_hearts),
    EIGHTH(8, R.drawable.eight_of_hearts),
    NINEH(9, R.drawable.nine_of_hearts),
    TENH(10, R.drawable.ten_of_hearts),
    JACKH(10, R.drawable.jack_of_hearts),
    QUEENH(10, R.drawable.queen_of_hearts),
    KINGH(10, R.drawable.king_of_hearts);

    private int value;
    private  int img;

    Pics( int value, int img){
        this.value = value;
        this.img = img;
    }

    public int getPic(){
        return img;
    }

    public int getValue(){
        return value;
    }
}

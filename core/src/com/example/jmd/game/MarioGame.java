package com.example.jmd.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

public class MarioGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture backgroud;
	Texture[] man;
	int manstate=0;
	int pause=0;
	float gravity=0.2f;
	float velocity=0;
	int manY=0;
	ArrayList<Integer> coinX=new ArrayList<Integer>();
	ArrayList<Integer> coinY=new ArrayList<Integer>();
	ArrayList<Integer> bombX=new ArrayList<Integer>();
	ArrayList<Integer> bombY=new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangle= new ArrayList<Rectangle>();
	ArrayList<Rectangle> bombRectangle= new ArrayList<Rectangle>();
	Rectangle manRectangle;
	int bombCount;
	int coinCount;
	Random random;
	Texture bomb;
	Texture coin;
	Texture dizzay;
	int score=0;
	BitmapFont font;
	int gameState=0;
	@Override
	public void create () {
		batch = new SpriteBatch();
        backgroud=new Texture("bg.jpg");
        man=new Texture[4];
        man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		manY=Gdx.graphics.getHeight() / 2;
		coin=new Texture("coin.jpg");
		bomb=new Texture("bomb.jpg");
		dizzay=new Texture("dizzay.png");
		random=new Random();
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

	}
public void makeCoin(){
		float height=random.nextFloat()* Gdx.graphics.getHeight();
		coinY.add((int)height);
		coinX.add(Gdx.graphics.getWidth());
}
public void makeBomb(){
	float height=random.nextFloat()*Gdx.graphics.getHeight();
	bombY.add((int)height);
	bombX.add(Gdx.graphics.getWidth());

}
	@Override
	public void render () {
		batch.begin();
		batch.draw(backgroud,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gameState==0)
		{
			if(Gdx.input.justTouched())
			{
				gameState=1;
			}
		}else if(gameState==1) {
			if (bombCount < 250) {
				bombCount++;
			} else {
				bombCount = 0;
				makeBomb();
			}
			bombRectangle.clear();
			for (int i = 0; i < bombX.size(); i++) {
				batch.draw(bomb, bombX.get(i), bombY.get(i));
				bombX.set(i, bombX.get(i) - 8);
				bombRectangle.add(new Rectangle(bombX.get(i), bombY.get(i), bomb.getWidth(), bomb.getHeight()));
			}
			if (coinCount < 100) {
				coinCount++;
			} else {
				coinCount = 0;
				makeCoin();
			}
			coinRectangle.clear();
			for (int i = 0; i < coinX.size(); i++) {
				batch.draw(coin, coinX.get(i), coinY.get(i));
				coinX.set(i, coinX.get(i) - 4);
				coinRectangle.add(new Rectangle(coinX.get(i), coinY.get(i), coin.getWidth(), coin.getHeight()));
			}
			if (Gdx.input.justTouched()) {
				velocity = -10;
			}
			if (pause < 30) {
				pause++;
			} else {
				if (manstate < 3) {
					manstate++;
				} else {
					manstate = 0;
				}
				velocity += gravity;
				manY -= velocity;
				if (manY <= 0) {
					manY = 0;
				}
			}
		}else if(gameState==2)
		{


			bombRectangle.clear();
			coinX.clear();
			coinY.clear();
			coinRectangle.clear();
			bombX.clear();
			bombY.clear();
			if(Gdx.input.justTouched())
			{
				gameState=1;
				manY=Gdx.graphics.getHeight() / 2;
				velocity=0;
				score=0;
				bombRectangle.clear();
				coinX.clear();
				coinY.clear();
				coinRectangle.clear();
				bombX.clear();
				bombY.clear();
				coinCount=0;
				bombCount=0;
			}


		}
          if(gameState==2)
		  {
			  batch.draw(dizzay, Gdx.graphics.getWidth() / 2 - dizzay.getWidth() / 2, manY);
		  }else {
			  batch.draw(man[manstate], Gdx.graphics.getWidth() / 2 - man[manstate].getWidth() / 2, manY);
		  }
			manRectangle= new Rectangle(Gdx.graphics.getWidth() / 2 - man[manstate].getWidth() / 2,manY,man[manstate].getWidth(),man[manstate].getHeight());
			for(int i=0;i<coinRectangle.size();i++)
			{
              if(Intersector.overlaps(manRectangle,coinRectangle.get(i))){
                  score++;
                  coinRectangle.remove(i);
                  coinX.remove(i);
                  coinY.remove(i);
                  break;
			  }
			}

		for(int i=0;i<bombRectangle.size();i++)
		{
			if(Intersector.overlaps(manRectangle,bombRectangle.get(i))){
				gameState=2;
				break;
			}
		}
		font.draw(batch,String.valueOf(score),100,200);
		batch.end();
	}
	@Override
	public void dispose () {
		batch.dispose();

	}
}

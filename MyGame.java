 import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.*;
public class MyGame 
{
    ChanDown frame;
    int width = 20;
    int height = 15;
    int x;
    int y;
    int sprite_x;
    int sprite_y;
    boolean bAnimate;
    String avatar;
    String background[][];
    String canvas[][];
    int score;
    int spriteCount = 5;
    boolean canDrop = true;
    boolean canAdd = true;
    int drops = 5;
    public static void main(String[] args)
    {

        MyGame m = new MyGame();    
    }
   
    MyGame() 
    {
        avatar = "o";
        x = 10;
        y = 2;
        
        setbackground();
        frame = new ChanDown();
        frame.setVisible(true);
        frame.textArea.addKeyListener(new AL());
        while(true)
        {
            try 
            {
                // thread to sleep for 1000 milliseconds
                Thread.sleep(175);
                animate();
            } 
            catch (Exception e)
            {
                System.out.println(e);
            }
        }
    }
    
    void animate()
    {
        if (bAnimate)
        {
            if (sprite_y < height)
                sprite_y++;
            draw();
        }
    }

    void clear()
    {
        frame.textArea.setText("");
    }

    void drawPlayer()
    {
        for (int i = 0; i < avatar.length();i++)
            if (x+ i < width)
                canvas[y][x+ i] = avatar.substring(i,i+1);
    }

    void drawSprite()
    {
        double a = 0; 
        if(canvas[sprite_y + 1][sprite_x].equals("*") || canvas[sprite_y + 1][sprite_x].equals("|"))
        {
            a = Math.random();
            if(a > .5)
                sprite_x+=1; 
            if(a < .5)
                sprite_x-=1; 
        }
        if(canvas[sprite_y][sprite_x].equals("_"))
        {
            bAnimate = false; 
            keepScore();
            
        }
        if(sprite_x == 0)
        {
            sprite_x++; 
        }
        
        if(sprite_x == width - 1)
        {
            sprite_x--; 
        }

        if (sprite_y < height){
            if(sprite_x == 0 && sprite_y == 0)
            canvas[sprite_y][sprite_x] = " ";
        else
            canvas[sprite_y][sprite_x] = "o";}
        else{
            bAnimate = false;
          
        }
    }
    void keepScore()
    {
        if(!bAnimate && canAdd){
        try{
            int r = 0;
            String s = canvas[sprite_y + 1][sprite_x];
            r = Integer.parseInt(s);
            score+= r;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
       }   
       canDrop = true; 
       canAdd = false;
    }
    void drawBackGround()
    {
        for (int i = 0;i<height;i++)
            for (int j = 0;j<width;j++)
                canvas[i][j] = background[i][j];

    }

    void draw()
    {
        clear();
        drawBackGround();
        drawPlayer();
        drawSprite();
        for (int i = 0;i<height;i++)
        {
            for (int j = 0;j<width;j++)
            {
                frame.textArea.append(canvas[i][j]);
            }
            frame.textArea.append("\n");
        }

        frame.textArea.append("Score: " + score + "\n");
        
        frame.textArea.append("Number of Drops Remaining: " + drops + "\n");
        
        frame.textArea.append("Score at or above 25 points" + "\n" + "to WIN!" + "\n");
        if(drops == 0){
            if(score < 25)
                frame.textArea.append("You lose! Press 'R' to restart." + "\n");
            if(score >= 25)
                frame.textArea.append("You win! Press 'R' to" + "\n" + "restart.");
        }
    }


    boolean isClear(int x, int y)
    {
        for (int i = 0; i < avatar.length();i++)
            if (x + i < width && background[y][x+i].equals("|") ||
            x + i < width && background[y][x+i].equals("-") ||
            x + i < width && background[y][x+i].equals("X") || x + i < width && background[y][x+i].equals("_"))
            
                return false;
        return true;
    }

    void left()
    {
        if (x>0 && isClear(x-1, y)) x--;
    }

    void right()
    {
        if (x<width-1 && isClear(x+1, y)) x++;
    }

    void down()
    {
        if (y<height-1 && isClear(x, y+1)) y++;
    }

    void up()
    {
        if (y>0  && isClear(x, y-1)) y--;
    }
    
    void spacebar()
    {
        bAnimate = true;
        sprite_x = x;
        sprite_y = y;
    }
    

    void setbackground()
    {
        String content = null;
        String rows[] = null;

        try
        {
            content = new String(Files.readAllBytes(Paths.get("sample.txt")));
            rows = content.split("\r\n");

        } catch (IOException e) {
            System.out.println(e);
        }
        if (content == null)
        {
            background = new String[height][width];
            for (int j = 0; j < height;j++)
            {
                for (int i = 0; i < width ;i++)
                {
                    if (i == 0 || i == width-1)
                        background[j][i] = "|";
                    else if (j == 0 || j == height-1)
                        background[j][i] = "-";
                    else
                        background[j][i] = " ";
                }
            }
        }
        else
        {
            height = rows.length;
            for (int i = 0; i < rows.length; i++)
                if (rows[i].length() > width) width = rows[i].length();
                
            background = new String[height][width];

            for (int i = 0; i < rows.length;i++)
                for (int j = 0; j < rows[i].length() ;j++)
                    background[i][j] = rows[i].substring(j,j + 1);
        }
        canvas = new String[height][width];
    }
    public class AL extends KeyAdapter
    {
        AL()
        {
            draw();
        }

        @Override
        public void keyPressed(KeyEvent event) 
        {
            int keyCode = event.getKeyCode();

            if (keyCode == KeyEvent.VK_SPACE) 
            {
                if(spriteCount > 0 && canDrop)
                    {
                        spacebar();
                        spriteCount--; 
                        canDrop = false; 
                        canAdd = true; 
                        drops--; 
                    }
            } 
            
            if (keyCode == event.VK_LEFT)
            {
                left();
            }
            if (keyCode == event.VK_RIGHT)
            {
                right();
            }
            
            if (keyCode == event.VK_R)
            {
                drops = 5;
                spriteCount = 5;
                score = 0;
            }
            /*if (keyCode == event.VK_DOWN)
            {
                down();
            }*/
            draw();
        }

        @Override
        public void keyReleased(KeyEvent event)
        {
        }

    }
    
}
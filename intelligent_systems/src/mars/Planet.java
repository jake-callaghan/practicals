package mars;

public class Planet {
    private final int[][] accessible = new int[][] {
        {1,1,1,1,0,1,1,1},
        {1,1,0,0,0,0,0,1},
        {1,0,1,0,1,0,1,1},
        {0,0,1,1,1,0,0,0},
        {1,0,0,1,0,1,0,1},
        {1,1,0,0,0,1,0,1},
        {1,1,1,0,0,0,0,1},
        {1,1,1,1,0,1,1,1},
     };
        
        
   
    
    public boolean isAccessible(int x, int y) {
        int offx = x+0; int offy = y+0;
        if (offx>=0 && offy>=0 && offx<accessible.length && offy<accessible[0].length) return (accessible[offx][offy]==0);
        else return false;
    }
    
    public int height() {return accessible.length;}
    public int width() {return accessible[0].length;}
}

import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int laps = in.nextInt(); //laps to complete the race
        int checkpointCount = in.nextInt(); //total checkpoints of the race
        int[][] checkpoints = new int[checkpointCount][2];
        for (int i = 0; i < checkpointCount; i++) { //coordinates of the checkpoints
            checkpoints[i][0] = in.nextInt();
            checkpoints[i][1] = in.nextInt();
        }

        Pod myPod1 = null, myPod2 = null, enemyPod1 = null, enemyPod2 = null;
        int[] newDirection = new int[2];
        int[] lastCheckpoint = new int[2];
        int[] nextCheckpoint = new int[2];
        int distance, thrust, nextCheckpointAngle;
        boolean boosted = false;

        // game loop
        while (true) {
            for (int i = 0; i < 2; i++) {
                if(i == 0){ //info of player pod 1
                    myPod1 = new Pod(in.nextInt(), in.nextInt(), in.nextInt(), 
                    in.nextInt(), in.nextInt(), in.nextInt());
                }
                else{ //info of player pod 2
                    myPod2 = new Pod(in.nextInt(), in.nextInt(), in.nextInt(), 
                    in.nextInt(), in.nextInt(), in.nextInt());
                }
            }
            for (int i = 0; i < 2; i++) {
                if(i == 0){ //info of enemy pod 1
                    enemyPod1 = new Pod(in.nextInt(), in.nextInt(), in.nextInt(), 
                    in.nextInt(), in.nextInt(), in.nextInt());
                }
                else{ //info of enemy pod 2
                    enemyPod2 = new Pod(in.nextInt(), in.nextInt(), in.nextInt(), 
                    in.nextInt(), in.nextInt(), in.nextInt());
                }
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            // You have to output the target position
            // followed by the power (0 <= thrust <= 100)
            // i.e.: "x y thrust"
            for(int i = 0; i < 2; i++){
                if(i == 0){ //pod 1 order
                    lastCheckpoint = myPod1.getLastCheckpoint(checkpoints);
                    nextCheckpoint = checkpoints[myPod1.nextCheckPointId];
                    distance = calcDistance(myPod1.x, myPod1.y, nextCheckpoint[0], nextCheckpoint[1]);
                    nextCheckpointAngle = (int)calcNextCheckpointAngle(myPod1, nextCheckpoint);

                    newDirection = calcDirection(lastCheckpoint[0], lastCheckpoint[1],
                    nextCheckpoint[0], nextCheckpoint[1], distance);

                    thrust = calcThrust(nextCheckpointAngle, distance);

                    if(!boosted && distance > 3000 && nextCheckpointAngle < 10 && nextCheckpointAngle > -10){
                        boosted = true;
                        System.out.printf("%d %d BOOST\n", newDirection[0], newDirection[1]);
                    }
                    else{
                        System.out.printf("%d %d %d\n", newDirection[0], newDirection[1], thrust);
                    }

                    System.err.println(myPod1.angle);
                    System.err.println(calcNextCheckpointAngle(myPod1, nextCheckpoint));
                }
                else{ //pod 2 order
                    lastCheckpoint = myPod2.getLastCheckpoint(checkpoints);
                    nextCheckpoint = checkpoints[myPod2.nextCheckPointId];
                    distance = calcDistance(myPod2.x, myPod2.y, nextCheckpoint[0], nextCheckpoint[1]);
                    nextCheckpointAngle = (int)calcNextCheckpointAngle(myPod2, nextCheckpoint);
                    
                    newDirection = calcDirection(lastCheckpoint[0], lastCheckpoint[1],
                    nextCheckpoint[0], nextCheckpoint[1], distance);

                    thrust = calcThrust(nextCheckpointAngle, distance);

                    if(!boosted && distance > 3000 && nextCheckpointAngle < 10 && nextCheckpointAngle > -10){
                        boosted = true;
                        System.out.printf("%d %d BOOST\n", newDirection[0], newDirection[1]);
                    }
                    else{
                        System.out.printf("%d %d %d\n", newDirection[0], newDirection[1], thrust);
                    }
           
                    System.err.println(myPod2.angle);
                    System.err.println(calcNextCheckpointAngle(myPod2, nextCheckpoint));
                }
            }
        }
    }

    //methods
    /***********************************************************/
    public static int[] calcDirection(int lChX, int lChY, int chX, int chY, int dist) {
    	int[] newDirection = new int[2]; //0 is the new X and 1 is the new Y
	
    	if(lChX < chX) { //if last checkpoint was in the left of the next checkpoint
        	newDirection[0] = chX - 400;
    	}
    	else {
        	newDirection[0] = chX + 400;
    	}    	

    	if(lChY < chY) { //if last checkpoint was in top of the next checkpoint
    		newDirection[1] = chY - 400;
    	}
    	else {
    		newDirection[1] = chY + 400;
    	}
    
        return newDirection;
    }

    //method that calculates and returns the distance between two points
    public static int calcDistance(int x1, int y1, int x2, int y2){
        int q1, q2;
        q1 = (x2 - x1) * (x2 - x1);
        q2 = (y2 - y1) * (y2 - y1);

        return (int)Math.sqrt(q1 + q2);
    }

    //method that calculates and returns the thrust to use
    public static int calcThrust(int nextCheckpointAngle, int nextCheckpointDist){
        int thrust;
        if(nextCheckpointAngle > 90 || nextCheckpointAngle < -90){
            thrust = 20;
        }
        else if(nextCheckpointDist < 3000){
            thrust = 90;
        }
        else if(nextCheckpointDist < 2000){
            thrust = 80;
        }
        else{
            thrust = 100;
        }

        return thrust;
    }

    //method that calculates and returns the angle to the next checkpoint
    public static double calcNextCheckpointAngle(Pod pod, int[] nextCheckpoint){
        int a, b, c;
        double angleC;

        a = calcDistance(pod.x, pod.y, 18000, pod.y); //dist between pod and 0
        b = calcDistance(pod.x, pod.y, nextCheckpoint[0], nextCheckpoint[1]); //dist between pod and next checkpoint
        c = calcDistance(nextCheckpoint[0], nextCheckpoint[1], 18000, pod.y); //dist between next checkpoint and 0

        angleC = (Math.acos((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2))/(2 * a * b)));

        if(pod.angle <= 180){
            return Math.toDegrees(angleC) - pod.angle;
        }
        else {
            return Math.toDegrees(angleC) - (360 - pod.angle);
        }



    }
}




//classes
/***********************************************************/
class Pod {
    int x, y, vx, vy, angle, nextCheckPointId;

    public Pod(int x, int y, int vx, int vy, int angle, int nextCheckPointId){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.nextCheckPointId = nextCheckPointId;
    }

    int[] getLastCheckpoint(int[][] checkpoints){
        if(nextCheckPointId == 0){
            return checkpoints[checkpoints.length-1];
        }
        else{
            return checkpoints[nextCheckPointId-1];
        }
    }
}

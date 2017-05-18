import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
//import java.io.InputStreamWriter;


class Node{

    public final int MAXSIZE = 2147483647;
    public int depth;
    public int player;
    public int sticksLeft;
    public int gameState;
    public Node[] children;

    public Node(int depth, int player, int sticksLeft, int gameState){

        this.depth = depth;
        this.player = player;
        this.sticksLeft = sticksLeft;
        this.children = createChildren();

    }

    public Node[] createChildren(){

        int value = 0;
        Node[] node = new Node[2];

        if (this.depth >= 0){

            for(int i = 0; i < 2; ++ i){

                value = this.sticksLeft -1;
                node[i] = new Node(this.depth - 1, this.player*-1,
                            value, this.realValue(value));
                //BufferedWriter br = new BufferedWriter(new FileWriter());
                //System.out.println("" + node);

            }

        }

        return node;

    }

    public int realValue(int value){

        if (value == 0){

            return MAXSIZE * this.player;

        }else if(value < 0){

            return MAXSIZE * this.player * -1;

        }

        return 0;

    }

}

public class StickGame{

    public static final int MAXSIZE = 2147483647;

    public static boolean hasWon(int sticks, int currentPlayer){

        if(sticks <= 0){

            if(currentPlayer > 0){

                if(sticks == 0){

                    System.out.println("Você VENCEU!");

                }else{

                    System.out.println("Você perdeu! Tirou palitos demais.");

                }

            }else{

                if(sticks == 0){

                    System.out.println("O computador VENCEU!");

                }else{

                    System.out.println("Erro computacional.");

                }

            }
            return false;

        }
        return true;

    }

    public static int MiniMax(Node node, int depth, int currentPlayer){

        int bestValue = 0;
        Node child;
        int value = 0;

        if((depth == 0) || Math.abs(node.gameState) == MAXSIZE){

            return node.gameState;

        }

        bestValue = MAXSIZE * currentPlayer * -1;
        for(int i = 0; i < node.children.length; ++i){

            child = node.children[i];
            value = MiniMax(child, depth-1, currentPlayer*-1);
            if(Math.abs(currentPlayer*MAXSIZE-value) <=
                Math.abs(currentPlayer*MAXSIZE-bestValue)){

                    bestValue = value;
            }

        }

        return bestValue;

    }

    public static void main(String[]args){

        int sticksTotal = 13;
        int depth = 4;
        int currentPlayer = 1;
        int sticksChoice = 0;
        int bestChoice = 0;
        int bestValue = 0;
        int value = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        Node node;
        Node child;
        boolean remove = false;

        System.out.println("Escolha entre um e dois palitos para remover. \n" +
                    "Seja o último jogador a remover um palito para vencer.");
        while(sticksTotal > 0){

            remove = false;

            System.out.println("Ainda há "+sticksTotal+" palitos. Deseja retirar"+
                                " um ou dois palitos?");

            while(!remove){

                try{
                    input = br.readLine();
                    sticksChoice = Integer.parseInt(input);
                }catch(Exception e){System.out.println("Digite apenas números entre um e dois.");}

                if(sticksChoice < 1 || sticksChoice > 2){

                    System.out.println("Favor selecionar novamente. Apenas um ou dois palitos.");

                }else{

                    remove = true;

                }

            }
            sticksTotal -= sticksChoice;

            if(hasWon(sticksTotal, currentPlayer)){

                currentPlayer *= -1;
                node = new Node(depth, currentPlayer, sticksTotal, 0);
                bestChoice = -100;
                bestValue = currentPlayer * MAXSIZE * -1;

                for(int i = 0; i < node.children.length; ++i){

                    child = node.children[i];
                    value = MiniMax(child, depth, currentPlayer*-1);
                    if(Math.abs(currentPlayer*MAXSIZE-value) <=
                        Math.abs(currentPlayer*MAXSIZE-bestValue)){

                            bestValue = value;
                            bestChoice = i;

                        }
                }

                bestChoice += 1;
                System.out.println("O computador removeu " + bestChoice);
                sticksTotal -= bestChoice;
                hasWon(sticksTotal, currentPlayer*-1);
                currentPlayer *= -1;

            }

        }

    }

}

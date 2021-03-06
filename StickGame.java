import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
//import java.io.OutputStreamWriter;


class Node{

    public final int MAXSIZE = 2147483647;
    public int depth; //o quao fundo estamos na árvore. Começa com por exemplo 4 e vai decrementando até 0
    public int player; //+1 ou -1
    public int sticksLeft; //quantos palitos sobram nessa escolha
    public int gameState; //valor do nó (-infinito, 0 ou +infinito)
    public Node[] children;

    public Node(int depth, int player, int sticksLeft, int gameState){

        this.depth = depth;
        this.player = player;
        this.sticksLeft = sticksLeft;
        this.children = createChildren();

    }

    public Node[] createChildren(){

        int sticksLeft = 0;
        Node[] node = new Node[3];

        if (this.depth >= 0){ //teste pra saber se acabaram os níveis da árvore

            for(int i = 1; i < 3; ++ i){ //loop para 1 ou 2 palitos

                sticksLeft = this.sticksLeft -1;
                node[i] = new Node(this.depth - 1, //minimiza a profundidade por 1
                                    this.player*-1, //troca o jogador
                                    sticksLeft, this.realValue(sticksLeft));
                //BufferedWriter br = new BufferedWriter(new FileWriter());
                //System.out.println("" + node);

            }

        }

        return node;

    }

    public int realValue(int value){

        if (value == 0){ //o jogador ganhou

            return MAXSIZE * this.player;

        }else if(value < 0){ //o jogador passou da quantidade de palitos e perdeu

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

        if((depth == 0) || Math.abs(node.gameState) == MAXSIZE){ //checa se chegamos ao fim da árvore ou se estamos em um nó que indica a vitória de qualquer um dos jogadores

            return node.gameState;

        }

        bestValue = MAXSIZE * currentPlayer * -1; //salva o pior caso (se vc eh o max, salva o menor possivel, e vice-versa)
        for(int i = 1; i < node.children.length; ++i){ //itera pelos filhos

            child = node.children[i];
            value = MiniMax(child, depth-1, currentPlayer*-1); //recebe o valor do nó (pode vir da condição de parada ou do fim do método)
            if(Math.abs(currentPlayer*MAXSIZE-value) <
                Math.abs(currentPlayer*MAXSIZE-bestValue)){

                    bestValue = value; //atualiza o melhor valor
            }

        }

        return bestValue; //retorna o melhor valor

    }

    public static void main(String[]args){

        int sticksTotal = 11;
        int depth = 4;
        int currentPlayer = 1; //jogador humano
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

                for(int i = 1; i < node.children.length; ++i){

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

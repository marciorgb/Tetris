import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Tetris extends JPanel {

	private static final long serialVersionUID = -8715353373678321308L;
    // VARIÁVEIS GLOBAIS DA CLASSE
	private Point pecaOrigem;
	private int pecaAtual;
	private int proxPeca;
	private int rotacao;
	private ArrayList<Integer> Proximas = new ArrayList<Integer>();

	private long Placar;
	private Color[][] menu;
	private Color[][] parede;
	private float dificult = 1;
	// CONSTANTES GLOBAIS 
	public static final Color corFundo = Color.BLACK;
	public static final Color corBorda = Color.DARK_GRAY;
	public static final Color corMenu = Color.LIGHT_GRAY;
	public static final int LINHAS_JOGO = 12;
	public static final int COLUNAS_JOGO = 24;
	public static final int MENU_TAM = 8;
	public static final int fontSize = 25;
	
	
	// VARIAEIS DE CONTROLE
	boolean pause = true;
	boolean over = false;
	boolean win = false;
	boolean esq = false;

	// MÉTODO QUE IMPLEMENTA OS BLOCOS [BLOCO][LISTA][POSICAO[X][Y]]
	private final Point[][][] Bloquinhos = {
			// I-Piece
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
			},
			
			// J-Piece
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
			},
			
			// L-Piece
			{
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
				{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
			},
			
			// O-Piece
			{
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
			},
			
			// S-Piece
			{
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
				{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
			},
			
			// T-Piece
			{
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
				{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
				{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
			},
			
			// Z-Piece
			{
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
				{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
				{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
			}
	};
	// MÉTODO QUE IMPLEMENTA AS CORES DOS BLOCOS
	private final Color[] bloquinhosCor = {
		Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.pink, Color.red
	};
	// CRIA A BORDA INICIAL E ADICIONA NOVA PEÇA
	private void init() {
		parede = new Color[ LINHAS_JOGO ][ COLUNAS_JOGO * 26 ];

		for (int i = 0; i < LINHAS_JOGO; i++) {
			for (int j = 0; j < COLUNAS_JOGO * 26; j++) {
				if (i == 0 || i == (LINHAS_JOGO -1) || j == (COLUNAS_JOGO - 2) ) {
					parede[i][j] = corBorda;
				} 
				else if ( j >= COLUNAS_JOGO){
					parede[i][j] = corMenu;
				}
				else {
					parede[i][j] = corFundo;
				}
			}
		}
		novaPeca();
	}
	// IMPLEMENTA CONDIÇÃO DE VITÓRIA E DERROTA
	public boolean game_over(Point a){
		if(parede[a.x][a.y] != corFundo){
			over = true;
			win = false;
			return true;
		}
		if(Placar == 10000){
			win = true;
		}
		return false;
	}
	// TENTA IMPLEMENTAR ESXAPE
	public void escape(){
		esq = ! esq;
	}
	// ADICIONA UMA NOVA PEÇA NA ORIGEM DO JOGO
	public void novaPeca() {
		pecaOrigem = new Point(4, 1);
		rotacao = 0;
		if(!game_over(pecaOrigem)){
			if (Proximas.isEmpty()) {
				Collections.addAll(Proximas, 0, 1, 2, 3, 4, 5, 6);
				Collections.shuffle(Proximas);
			}
			pecaAtual = Proximas.get(0);
			Proximas.remove(0);
			if(!game_over(pecaOrigem) && !Proximas.isEmpty()){
				proxPeca = Proximas.get(0);
			}
		}

	}
	// TESTE DE COLIZÃO PARA A PEÇA
	private boolean testeColisao(int x, int y, int rotacao) {
		for (Point p : Bloquinhos[pecaAtual][rotacao]) {
			if (parede[p.x + x][p.y + y] != corFundo) {
				return true;
			}
		}
		return false;
	}
	// ROTACIONA
	public void rotate(int i) {
		int novaRotacao = (rotacao + i) % 4;
		if (novaRotacao < 0) {
			novaRotacao = 3;
		}
		if (!testeColisao(pecaOrigem.x, pecaOrigem.y, novaRotacao)) { // VERIFICA SE A ROTAÇÃO É POSSÍVEL
			rotacao = novaRotacao;
		}
		repaint();
	}
	// MOVIMENTA A PEÇA
	public void move(int i) {
		if (!testeColisao(pecaOrigem.x + i, pecaOrigem.y, rotacao)) { // VERIFICA COLIZÃO LATERAL E PAREDE
			pecaOrigem.x += i;	
		}
		repaint();
	}
	// IMPLEMENTA DIFICULDADE
	public void dificuldade(){
		if(Placar > 1000 && dificult <= 2){
			dificult += 0.2;
		}
		else if(Placar > 4000 && dificult <= 3){
			dificult++;
		}
		else if(Placar > 9000 && dificult <= 4){
			dificult++;
		}
	}
	// IMPLEMENTA A QUEDA DO BLOCO, E VERIFICA SE HÁ PAREDE E A TORNA UMA AO FINAL
	public void dropDown() {
		dificuldade(); // DIFICULDADE BASEADA NO TEMPO
		if (!testeColisao(pecaOrigem.x, pecaOrigem.y + 1, rotacao)) {
			pecaOrigem.y += 1;
		} else {
			fixToparede();
		}	
		repaint();
	}
	// FAZ A PEÇA AGORA, PARTE DA PAREDE PARA VERIFICAR COLIZÃO
	public void fixToparede() {
		for (Point p : Bloquinhos[pecaAtual][rotacao]) {
			parede[pecaOrigem.x + p.x][pecaOrigem.y + p.y] = bloquinhosCor[pecaAtual];
		}
		limpaLinhas();
		novaPeca();
	}
	// PUXA AS PEÇAS UMA LINHA PARA BAIXO
	public void deleteRow(int row) {
		for (int j = row-1; j > 0; j--) {
			for (int i = 1; i < 11; i++) {
				parede[i][j+1] = parede[i][j];
			}
		}
	}
	// LIMPA AS LINHAS COMPLENTAMENTE 
	public void limpaLinhas() {
		boolean gap;
		int numLimpas = 0;
		// VERIFICA SE TODA A LINHA É DIFERETE DE PRETO
		for (int j = 21; j > 0; j--) {
			gap = false;
			for (int i = 1; i < 11; i++) {
				if (parede[i][j] == corFundo) { 
					gap = true;
					break;
				}
			}
			if (!gap) {
				deleteRow(j);
				j += 1;
				numLimpas += 1;
			}
		}
		
		switch (numLimpas) {
		case 1:
			Placar += 100;
			break;
		case 2:
			Placar += 300;
			break;
		case 3:
			Placar += 500;
			break;
		case 4:
			Placar += 800;
			break;
		}
	}
	// VERIFICA A POSIÇÃO TEÓRICA DOS BLOCOS
	public int checaPos(){
	
            ArrayList<Integer> theor = new ArrayList<>();
            for (Point p : Bloquinhos[pecaAtual][rotacao]) {
                int valorTeorico = 0;
                for(int j = p.y + pecaOrigem.y + 1; j < COLUNAS_JOGO - 2 ; j++){
                    if(parede[p.x + pecaOrigem.x][j] == corFundo){
                        valorTeorico++;
                    }else break;
                }
                theor.add(valorTeorico);
            }
            return Collections.min(theor) + pecaOrigem.y;
    }
	// DESENHA A PEÇA ENQUANTO CAI
	private void drawPiece(Graphics g) {
		// VERIFICA A PRÓXIMA PEÇA
		//DESENHA O PROXIMO
		if(!over){
			g.setColor(bloquinhosCor[proxPeca]);
			for (Point p : Bloquinhos[proxPeca][1]) { 

				g.fillRect((p.x + LINHAS_JOGO) * 26+75, (p.y + 4) * 26, 25, 25);
			}
		}
		// PINTA A POSIÇÃO TEÓRICA
		g.setColor(Color.GRAY);
		for (Point p : Bloquinhos[pecaAtual][rotacao]) {
			g.fillRect((p.x + pecaOrigem.x) * 26, (p.y + checaPos()) * 26, 25, 25);
		}
		// PINTA O BLOCO EM SI
		g.setColor(bloquinhosCor[pecaAtual]);
		for (Point p : Bloquinhos[pecaAtual][rotacao]) {

			g.fillRect((p.x + pecaOrigem.x) * 26, (p.y + pecaOrigem.y) * 26, 25, 25);
		}

	}
	// TENTATIVA DE DESENHAR O MENU 
	private void drawMenu(Graphics g){
		menu = new Color[ LINHAS_JOGO * 26 ][ COLUNAS_JOGO * 26 ];
		
		for (int i = 0; i < LINHAS_JOGO; i++) {
			for (int j = 0; j < COLUNAS_JOGO * 26; j++) {
				if (i == LINHAS_JOGO + 1 || i == (LINHAS_JOGO + 7) || j == (COLUNAS_JOGO/2) ) {
					menu[i][j] = corBorda;
				}
				else {
					menu[i][j] = corFundo;
				}
			}
		}
	}

	@Override 
	public void paintComponent(Graphics g)
	{
		// PINTA A WALL
		g.fillRect(0, 0, 10 + 26 * (LINHAS_JOGO + MENU_TAM),(COLUNAS_JOGO)* 26 +25);
		for (int i = 0; i < LINHAS_JOGO; i++) {
			for (int j = 0; j < COLUNAS_JOGO * 26; j++) {
				if ( j > COLUNAS_JOGO*26){
					parede[i][j] = menu[i][j]; 
				}
				g.setColor(parede[i][j]);
				g.fillRect(26*i, 26*j, 25, 25);
			}
		}
		// MOSTRA O LOZE	
		if(over && !win){
			g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize + 25));
			g.setColor(Color.getHSBColor(191,53,50));
			g.drawString("Game Over!", LINHAS_JOGO*10, 300);
			g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
			g.drawString("Score: " + Placar, LINHAS_JOGO*10, 300 + fontSize);
		// MOSTRA O WIN 	
		}
		else if(!over && win){
			g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize + 25));
			g.setColor(Color.getHSBColor(191,53,50));
			g.drawString("You Win!", LINHAS_JOGO*10, 300);
			g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
			g.drawString("Score: " + Placar, LINHAS_JOGO*10, 300 + fontSize);
			
		}
		// MOSTRA O PLACAR
		else{
			g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));
			g.setColor(Color.BLACK);
			g.drawString("Score: " + Placar, 30*LINHAS_JOGO, 50);
			g.setFont(new Font("TimesRoman", Font.BOLD, fontSize/2));
			g.setColor(Color.BLACK);
			g.drawString("Dificuldade: " + (int)dificult, 30*LINHAS_JOGO, 50 + fontSize);
			g.setFont(new Font("TimesRoman", Font.BOLD, fontSize/2));
			g.setColor(Color.BLACK);
			g.drawString("Próxima: ", 30*LINHAS_JOGO, 50 + fontSize * 2);
		}
		drawMenu(g);
		// Draw the currently falling piece
		drawPiece(g);
	
	}
	// ADICIONA PAUSE
	private boolean pause_game()
	{
		return pause = !pause;
	}
	// MAIN DO JOGO
	public static void main(String[] args) {
		JFrame f = new JFrame("Tetris");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(10 + 26 * (LINHAS_JOGO + MENU_TAM),(COLUNAS_JOGO)* 26 +25);
		f.setVisible(true);
		
		final Tetris game = new Tetris();
		game.init();
		f.add(game);
		
		// Keyboard controls
		f.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}
			
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					if(!game.over){
						game.rotate(-1);
					}
					break;
				case KeyEvent.VK_DOWN:
					game.dropDown();
					if(!game.over){
						game.Placar += 1;
					}
					break;
				case KeyEvent.VK_LEFT:
					if(!game.over){
						game.move(-1);
					}
					break;
				case KeyEvent.VK_RIGHT:
					if(!game.over){
						game.move(+1);
					}
					break;
				case KeyEvent.VK_SPACE:
					game.pause_game();
					break;
				case KeyEvent.VK_ESCAPE:
					if(game.over){
						game.escape();
					}
					break;
				} 
			}
			
			public void keyReleased(KeyEvent e) {
			}
		});
		
		// FAZ A PEÇA CAIR A CADA SEGUNDO ACRESCIDO DA
		new Thread() {
			@Override public void run() {
				while (!game.esq) {
					try {
						if(!game.pause_game()){
							Thread.sleep( (int)((1000/(game.dificult/2)) ));
							game.dropDown();
						}
					} catch ( InterruptedException e ) {}
				}
			}
		}.start();
	}
}
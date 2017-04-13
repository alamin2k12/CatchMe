import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class GameFrame extends JFrame
{
	private RichPanel contentPane;

	private JLabel lblScore;
	// private JLabel lblUserInput;
	private JLabel currentObject;
	private JLabel lblLife;

	private JPanel gamePanel;
	private JPanel userPanel;

	private JTextField userInput;

	private ArrayList<GameObject> gameObjects;

	private int score;
	private int currentIndex;

	private int baseSpeed;
	private int currentSpeed;

	private int life;
	private int lifeRemaining;

	private float speedFactor;
	private float currentSpeedFactor;

	private int baseSteak;
	private int steak;
	private int currentSteak;

	private Timer timer;

	/**
	 * Create the frame.
	 */
	public GameFrame()
	{
		super("Catch Me If You Can!");

		// Frame Initializer
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 600);
		setLocation(400, 100);
		setResizable(false);

		contentPane = new RichPanel(new File("Background\\"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		Load();
		Initialize();
		Start();
	}

	private void Initialize()
	{
		// Loads game settings
		DefaultSettings();

		// Game Panel
		gamePanel = new JPanel();
		gamePanel.setOpaque(false);
		gamePanel.setLayout(null);
		getContentPane().add(gamePanel, BorderLayout.CENTER);

		currentObject = new JLabel();
		currentObject.setSize(100, 100);
		currentObject.setLocation(240, 0);
		currentObject.setOpaque(false);
		gamePanel.add(currentObject);

		lblScore = new JLabel();
		lblScore.setHorizontalAlignment(SwingConstants.CENTER);
		lblScore.setFont(new Font("Tahoma", Font.BOLD, 42));
		lblScore.setText("Score: 0");
		lblScore.setOpaque(false);
		getContentPane().add(lblScore, BorderLayout.SOUTH);

		// User Panel
		userPanel = new JPanel();
		userPanel.setOpaque(false);
		userPanel.setLayout(new BorderLayout());
		getContentPane().add(userPanel, BorderLayout.NORTH);

		// lblUserInput = new JLabel();
		// lblUserInput.setFont(new Font("Tahoma", Font.PLAIN, 21));
		// lblUserInput.setText("Enter name :");
		// userPanel.add(lblUserInput);

		lblLife = new JLabel();
		lblLife.setFont(new Font("Tahoma", Font.PLAIN, 21));
		lblLife.setHorizontalAlignment(SwingConstants.CENTER);
		lblLife.setText("Life: " + lifeRemaining);
		userPanel.add(lblLife, BorderLayout.NORTH);

		userInput = new JTextField();
		userInput.setFont(new Font("Tahoma", Font.PLAIN, 28));
		userInput.setHorizontalAlignment(SwingConstants.CENTER);
		userInput.setColumns(20);
		userInput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				String imageName = gameObjects.get(currentIndex).getName().toLowerCase();

				if (userInput.getText().toLowerCase().equals(imageName))
				{
					Success();
				}
				else
				{
					Failure();
				}
			}
		});
		userPanel.add(userInput, BorderLayout.CENTER);
	}

	private void Start()
	{
		contentPane.startSlideShow();

		currentIndex = getRandomIndex();

		Update();

		timer = new Timer(20, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				float fall = currentSpeed * currentSpeedFactor;
				int X = currentObject.getX();
				int Y = currentObject.getY() + (int) fall;

				int lowerBaseLine = gamePanel.getHeight();

				if (Y >= lowerBaseLine)
				{
					Failure();
					return;
				}

				currentObject.setLocation(X, Y);
			}
		});

		timer.start();
		ResetObjectLocation();
	}

	private void ResetObjectLocation()
	{
		currentObject.setLocation(240, 0);
		currentObject.setIcon(new ImageIcon(gameObjects.get(currentIndex)
				.getGameIcon().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
	}

	private void Load()
	{
		gameObjects = new ArrayList<>();

		File folder = new File("Images\\");

		File[] files = folder.listFiles();

		for (File file : files)
		{
			if (file.isFile()
					&& getFileExtension(file).toLowerCase().equals("png"))
			{
				try
				{
					Image image = ImageIO.read(file);
					String name = file.getName()
							.substring(0, file.getName().indexOf('.'))
							.toLowerCase();
					gameObjects.add(new GameObject(name, image));
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public String getFileExtension(File file)
	{
		String extension = file.getName().substring(
				file.getName().indexOf('.') + 1);

		return extension;
	}

	private int getRandomIndex()
	{
		return new Random().nextInt(gameObjects.size() - 1);
	}

	private void SpeedUpFall()
	{
		currentSpeedFactor += 0.25f;
	}

	private void Failure()
	{
		lifeRemaining--;

		if (lifeRemaining < 0)
		{
			timer.stop();
			GameOver();
			return;
		}

		Update();
		IncrementCurrentIndex();
		ResetObjectLocation();
	}

	private void Success()
	{
		score++;
		IncrementSteak();
		Update();
		IncrementCurrentIndex();

		ResetObjectLocation();
	}

	private void GameOver()
	{
		contentPane.stopSlideShow();

		// Game Over Dialog
		JDialog gameOver = new JDialog();
		gameOver.setTitle("GAME OVER");
		gameOver.setSize(600, 300);
		gameOver.setLocation(getX(), getY() + 150);
		gameOver.setUndecorated(true);
		gameOver.getContentPane().setLayout(new BorderLayout());
		gameOver.setModalityType(ModalityType.APPLICATION_MODAL);
		gameOver.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		gameOver.setResizable(false);

		// Button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		gameOver.add(buttonPanel, BorderLayout.SOUTH);
		
		// Score label
		JLabel finalScore = new JLabel();
		finalScore.setText("" + score);
		finalScore.setFont(new Font("Tahoma", Font.BOLD, 90));
		finalScore.setHorizontalAlignment(SwingConstants.CENTER);
		gameOver.getContentPane().add(finalScore, BorderLayout.CENTER);

		// Restart button
		JButton btnRestart = new JButton("RESTART");
		btnRestart.setFont(new Font("Tahoma", Font.PLAIN, 32));
		// btnRestart.setIcon(new ImageIcon("resources\\Button Restart.png"));
		btnRestart.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent e)
			{
				gameOver.dispose();
				Restart();
			}
		});
		buttonPanel.add(btnRestart);

		// Restart button
		JButton btnExit = new JButton("Exit");
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 32));
		// btnRestart.setIcon(new ImageIcon("resources\\Button Restart.png"));
		btnExit.addActionListener(new ActionListener() {

			
			public void actionPerformed(ActionEvent e)
			{
				gameOver.dispose();
				System.exit(0);
			}
		});
		buttonPanel.add(btnExit);

		gameOver.setVisible(true);
	}

	private void IncrementCurrentIndex()
	{
		currentIndex++;

		if (currentIndex >= gameObjects.size())
		{
			currentIndex = 0;
		}
	}

	private void IncrementSteak()
	{
		currentSteak++;

		if (currentSteak >= steak)
		{
			currentSteak = 0;
			SpeedUpFall();
		}

		if (steak > 1)
		{
			steak--;
		}
	}

	private void Update()
	{
		userInput.setText("");
		lblScore.setText("Score: " + score);
		lblLife.setText("Life: " + lifeRemaining);
	}

	private void Restart()
	{
		DefaultSettings();

		currentIndex = getRandomIndex();
		
		ResetObjectLocation();

		Update();

		timer.start();

		contentPane.startSlideShow();
	}

	private void DefaultSettings()
	{
		// Game Settings
		baseSpeed = 1;
		currentSpeed = baseSpeed;
		score = 0;
		speedFactor = 1.0f;
		currentSpeedFactor = speedFactor;
		life = 5;
		lifeRemaining = life;
		baseSteak = 5;
		steak = baseSteak;
		currentSteak = 0;
	}
}

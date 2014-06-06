package naftoreiclag.village.gamestates;

public abstract class GameState
{
	// Like run() in Runnable, but returns a new GameState for the Main class to run (to prevent a stack overflow error)
	public abstract GameState run();
}

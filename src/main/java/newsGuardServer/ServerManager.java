package newsGuardServer;
import engine.Engine;


public class ServerManager {
    Engine engine;
    AuthController authController;

    public ServerManager()
    {
        authController = new AuthController(engine);
    }
}

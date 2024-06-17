package newsGuardServer;
import logic.engine.Engine;

public class ServerManager {
    Engine engine = new Engine();
    AuthController authController;

    public ServerManager()
    {
        authController = new AuthController(engine);
    }
}

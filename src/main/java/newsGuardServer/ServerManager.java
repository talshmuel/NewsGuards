package newsGuardServer;
import logic.engine.Engine;
import logic.engine.Engine;

public class ServerManager {
    Engine engine;
    AuthController authController;

    public ServerManager()
    {
        authController = new AuthController(engine);
    }
}

package newsGuardServer;
import logic.engine.Engine;

public class ServerManager {
    Engine engine = new Engine();
    AuthController authController;
    ReportController reportController;
    LocationController locationController;

    public ServerManager()
    {
        authController = new AuthController(engine);
        reportController= new ReportController(engine);
        locationController= new LocationController(engine);
    }
}

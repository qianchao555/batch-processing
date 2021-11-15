package designpattern.builders.components;

/**
 * @ClassName GPSNavigator
 * @Author qianchao
 * @Date 2021/11/15
 * @Version OPRA V1.0
 **/
public class GPSNavigator {
    private String route;
    public GPSNavigator() {
        this.route = "221b, Baker Street, London  to Scotland Yard, 8-10 Broadway, London";
    }

    public GPSNavigator(String manualRoute) {
        this.route = manualRoute;
    }

    public String getRoute() {
        return route;
    }
}

#  `@Parent` is not loaded even though it has `@Load` annotation. Here's the relevant code.
    ```
    @Cache
    @Entity
    public class Order {
        @Id private Long id;
        @Parent @Load public Ref<Profile> profile;
        @Index private String title;
        @Load @Index List<Ref<Product>> products = new ArrayList<Ref<Product>>(10);
        Map<String, OrderedProduct> orderedProducts = new HashMap<String, OrderedProduct>(10);

        @SuppressWarnings("unused")
        private Order() {}
        
        public Order(final Profile profile,
                    final OrderForm orderForm
                ) {
                this.profile = Ref.create(Key.create(Profile.class, profile.getUserId()));

    ```
## Sample app to demo a @Load bug in objectify.
- `git clone https://github.com/askrht/order-demo.git`
- https://github.com/askrht/order-demo.git

## One time setup only if you want to deploy to appengine
- Replace WEB_CLIENT_ID in Constants.java
- Replace conferencecentral2 in appengine-web.xml
- Replace conferencecentral2 in pom.xml

## Maven commands
    mvn clean install
    mvn eclipse:clean
    mvn eclipse:eclipse
    mvn appengine:run

## Steps To reproduce the problem
1. Execute `mvn clean compile test`. Notice that the `OrderTest` passes. It is able to read the Profile.
1. Go to api explorer at http://localhost:8080/_ah/explorer
1. Create a product with id `a1` , no need to authenticate
1. Create an order, requires authentication, use this JSON
    {
        "orderQty": 
        [
            {
            "productId": "a1",
            "qty": "12"
            }
        ]
    }
1. You will see the following exception:
    ```
    java.io.IOException: com.fasterxml.jackson.databind.JsonMappingException: Direct self-reference leading to cycle (through reference chain: com.conferencecentral.api.domain.Order["profile"]->com.googlecode.objectify.impl.ref.LiveRef["key"]->com.googlecode.objectify.Key["root"])
	at com.google.api.server.spi.response.ServletResponseResultWriter.writeValueAsString(ServletResponseResultWriter.java:212)
	at com.google.api.server.spi.response.ServletResponseResultWriter.write(ServletResponseResultWriter.java:101)
	at com.google.api.server.spi.SystemService.invokeServiceMethod(SystemService.java:352)
	at com.google.api.server.spi.handlers.EndpointsMethodHandler$RestHandler.handle(EndpointsMethodHandler.java:119)
	at com.google.api.server.spi.handlers.EndpointsMethodHandler$RestHandler.handle(EndpointsMethodHandler.java:102)
	at com.google.api.server.spi.dispatcher.PathDispatcher.dispatch(PathDispatcher.java:50)
	at com.google.api.server.spi.EndpointsServlet.service(EndpointsServlet.java:71)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:790)
	at org.eclipse.jetty.servlet.ServletHolder.handle(ServletHolder.java:848)
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1772)
	at com.googlecode.objectify.ObjectifyFilter.doFilter(ObjectifyFilter.java:48)
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1759)
	at com.google.appengine.tools.development.ResponseRewriterFilter.doFilter(ResponseRewriterFilter.java:134)
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1759)
	at com.google.appengine.tools.development.HeaderVerificationFilter.doFilter(HeaderVerificationFilter.java:34)
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1759)
	at com.google.appengine.api.blobstore.dev.ServeBlobFilter.doFilter(ServeBlobFilter.java:63)
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1759)
	at com.google.apphosting.utils.servlet.TransactionCleanupFilter.doFilter(TransactionCleanupFilter.java:48)
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1759)
	at com.google.appengine.tools.development.jetty9.StaticFileFilter.doFilter(StaticFileFilter.java:123)
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1759)
	at com.google.appengine.tools.development.DevAppServerModulesFilter.doDirectRequest(DevAppServerModulesFilter.java:366)
	at com.google.appengine.tools.development.DevAppServerModulesFilter.doDirectModuleRequest(DevAppServerModulesFilter.java:349)
	at com.google.appengine.tools.development.DevAppServerModulesFilter.doFilter(DevAppServerModulesFilter.java:116)
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1759)
	at com.google.appengine.tools.development.DevAppServerRequestLogFilter.doFilter(DevAppServerRequestLogFilter.java:44)
	at org.eclipse.jetty.servlet.ServletHandler$CachedChain.doFilter(ServletHandler.java:1751)
	at org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:582)
	at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:143)
	at org.eclipse.jetty.security.SecurityHandler.handle(SecurityHandler.java:524)
	at org.eclipse.jetty.server.session.SessionHandler.doHandle(SessionHandler.java:226)
	at org.eclipse.jetty.server.handler.ContextHandler.doHandle(ContextHandler.java:1180)
	at org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:512)
	at org.eclipse.jetty.server.session.SessionHandler.doScope(SessionHandler.java:185)
	at org.eclipse.jetty.server.handler.ContextHandler.doScope(ContextHandler.java:1112)
	at com.google.appengine.tools.development.jetty9.DevAppEngineWebAppContext.doScope(DevAppEngineWebAppContext.java:94)
	at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:141)
	at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:134)
	at com.google.appengine.tools.development.jetty9.JettyContainerService$ApiProxyHandler.handle(JettyContainerService.java:597)
	at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:134)
	at org.eclipse.jetty.server.Server.handle(Server.java:534)
	at org.eclipse.jetty.server.HttpChannel.handle(HttpChannel.java:320)
	at org.eclipse.jetty.server.HttpConnection.onFillable(HttpConnection.java:251)
	at org.eclipse.jetty.io.AbstractConnection$ReadCallback.succeeded(AbstractConnection.java:283)
	at org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:108)
	at org.eclipse.jetty.io.SelectChannelEndPoint$2.run(SelectChannelEndPoint.java:93)
	at org.eclipse.jetty.util.thread.strategy.ExecuteProduceConsume.executeProduceConsume(ExecuteProduceConsume.java:303)
	at org.eclipse.jetty.util.thread.strategy.ExecuteProduceConsume.produceConsume(ExecuteProduceConsume.java:148)
	at org.eclipse.jetty.util.thread.strategy.ExecuteProduceConsume.run(ExecuteProduceConsume.java:136)
	at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:671)
	at org.eclipse.jetty.util.thread.QueuedThreadPool$2.run(QueuedThreadPool.java:589)
	at java.lang.Thread.run(Thread.java:745)
    ```
1. If we comment the following line in `Order.java`, the error will disappear but then the unit test will fail with a `NullPointerException` because the profile is not present.
    ```
    this.profile = Ref.create(Key.create(Profile.class, profile.getUserId()));
    ```
1. Execute `order.queryOrdersByProductsAdmin`, pass `a1` as the product id and you will get the same error.

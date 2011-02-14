spotlink.org

SpotLink consists of two primary components. The core provides the web services package, 
SCTE-130 interfaces and configuration.  The module subsystem provides connectivity to 
one or more external ad decision services.  To integrate your new decision engine into 
SpotLink, simply implement the PlacementDecisionEngine interface (or subclass 
AbstractPlacementDecisionEngine) and configure spotlink.properties accordingly.  

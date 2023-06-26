# Enhanced Map for Vaadin Flow

This project is an extension of Vaadin Map component that includes geofencing capabilities.

This component is part of Vaadin Component Factory.

## Running the component demo
Run from the command line:
- `mvn  -pl vcf-enhanced-map-demo -Pwar install jetty:run`

Then navigate to `http://localhost:8080/`

## Installing the component
Run from the command line:
- `mvn clean install -DskipTests`

## Profiles
### Profile "directory"
This profile, when enabled, will create the zip file for uploading to Vaadin's directory

### Profile "production"
This profile, when enabled, will execute a production build for the demo

## Using the component in a Flow application
To use the component in an application using maven,
add the following dependency to your `pom.xml`:
```
<dependency>
    <groupId>org.vaadin.addons.componentfactory</groupId>
    <artifactId>vcf-enhanced-map</artifactId>
    <version>${component.version}</version>
</dependency>
```

## License

Vaadin Commercial License
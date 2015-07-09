package jisop.test;

import jisop.Build;
import jisop.test.testData.FullConfiguration;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mathieu.
 */
class ConfigurationTestX {
    private String tryGetExamplePath(){
        throw new RuntimeException();
        /*return new string[] { "Debug", "Release" }.Select(configuration=>
                Path.GetFullPath(Path.Combine("..", "..", "..",
                        Path.Combine("Example", "bin", configuration)))
        ).FirstOrDefault(path=>Directory.Exists(path));*/
    }
    //@Test TODO: Implement
    public void Can_read_configuration_from_example_project(){
        String path = tryGetExamplePath();

        /*Build parserBuilder = new Build().configurationFrom(path);

        Assert.assertTrue(parserBuilder.getControllerRecognizers().size() >= 2);*/
    }
    // @Test TODO: Implement
    public void Can_read_documentation_for_properties(){
        FullConfiguration conf = new FullConfiguration();
        /*Build parserBuilder = new Build().configuration(conf);
        String globalDesc = parserBuilder.getGlobalParameters()
                .stream()
                //.filter(gp->gp.argument.prototype.equals("Global"))
                .findFirst().get().description;
        Assert.assertEquals("GLOBAL!!", globalDesc);*/
    }
}

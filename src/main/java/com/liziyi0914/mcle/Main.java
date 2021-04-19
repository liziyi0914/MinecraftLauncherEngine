package com.liziyi0914.mcle;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;

public class Main {

    private static Options OPTIONS = new Options();
    private static CommandLine commandLine;

    public static void main(String[] args) {
        CommandLineParser commandLineParser = new DefaultParser();
        OPTIONS.addOption(Option.builder("h").longOpt("help").desc("show help message").build());
        OPTIONS.addOption(Option.builder("r").required().hasArg(true).longOpt("root").type(String.class).desc("the path of .minecraft").build());
        OPTIONS.addOption(Option.builder("v").hasArg(true).longOpt("version").type(String.class).desc("game's version").build());
        OPTIONS.addOption(Option.builder("n").hasArg(true).longOpt("name").type(String.class).desc("player's name").build());
        OPTIONS.addOption(Option.builder("u").hasArg(true).longOpt("uuid").type(String.class).desc("player's uuid").build());
        OPTIONS.addOption(Option.builder("t").hasArg(true).longOpt("token").type(String.class).desc("player's token").build());

        try {
            commandLine = commandLineParser.parse(OPTIONS, args);
            if (commandLine.hasOption('h')) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("MCLEngine",OPTIONS);
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("MCLEngine",OPTIONS);
            System.exit(0);
        }

        MinecraftHome home = new MinecraftHome(new File(commandLine.getOptionValue("r")));
        Arguments arguments = new Arguments();
        arguments.setPlayer(
                commandLine.getOptionValue("n"),
                commandLine.getOptionValue("u"),
                commandLine.getOptionValue("t")
        );
        Launcher launcher = new Launcher(home,commandLine.getOptionValue("v"),arguments,home.getRoot().getAbsoluteFile());
        try {
            System.out.println(launcher.genCommandLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

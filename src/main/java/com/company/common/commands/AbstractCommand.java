package com.company.common.commands;

public abstract class AbstractCommand {
    public abstract String execute(String commandArgs);

    public String execute(String stingArgs, Object objArgs) {
        return execute(stingArgs);
    }

    public abstract String describe();
}
/*
interface iCommandPool {
public ServerAddCommand get(String cmdName);
        }

class CommandPool implements iCommandPool {
    public ServerAddCommand get(String cmdName){return  new ServerAddCommand();}
}

class CommonAddCommand {
    public void execute(iCommandPool cp){
        cp.get(describe()).execute("");
    }
    //public void execute(String stingArgs, Object objArgs) { execute(stingArgs); }

    public String describe() {return ";";}
}

class ServerAddCommand {
    public void execute(String commandArgs){}
    public void execute(String stingArgs, Object objArgs) { execute(stingArgs); }
}


interface iCmdMethods {
    public String add();
    public String remove();
}

class CmdMethods implements iCmdMethods {

    public String add() { (new ServerAddCommand()).execute(""); return "";}
    public String remove() {(new ServerAddCommand()).execute(""); return";";}
}

class CommonAddCommand2 {
    public void execute(iCmdMethods cm){
        cm.add();
    }
    //public void execute(String stingArgs, Object objArgs) { execute(stingArgs); }

    public String describe() {return ";";}
}

class CommonAddRemove2 {
    public void execute(iCmdMethods cm){
        cm.remove();
    }
    //public void execute(String stingArgs, Object objArgs) { execute(stingArgs); }

    public String describe() {return ";";}
}
*/
package demo.model;

import org.springframework.boot.autoconfigure.websocket.UndertowWebSocketContainerCustomizer;

import java.util.HashMap;

/**
 * Created by Khang on 02/07/2015.
 */
public class OwnerReturnValue extends BasicReturnValue {
    private Owner owner;

    public OwnerReturnValue(){};

    public OwnerReturnValue(Owner owner,HashMap errorList){
        super(errorList);
        this.owner = owner;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}

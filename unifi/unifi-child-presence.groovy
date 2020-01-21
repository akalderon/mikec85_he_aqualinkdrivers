metadata {
    definition (name: "Unifi Child Presence", namespace: "unifi", author: "MC") {
        capability "PresenceSensor"
        
      
        command "Update", null
    }

    preferences {
        section("Device Settings:") {
            input "mac_addr", "string", title:"Mac Address of Client to Track", description: "", required: true, displayDuringSetup: true, defaultValue: ""
            input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true
            input name: "autoUpdate", type: "bool", title: "Enable Auto updating", defaultValue: true
        }
    }


}

void installed() {
    log.warn "..."
    initialize()
}
def logsOff() {
    log.warn "debug logging disabled..."
    device.updateSetting("logEnable", [value: "false", type: "bool"])
}

void parse(String description) {
    

}
void initialize(){
    if (autoUpdate) runIn(600, Update)
    
}

void setmac(String MAC) {
       device.updateSetting("mac_addr", [value: "${MAC}", type: "string"])
}

void Update(){
    def status2 = false
    status2 = parent.ChildGetClientConnected(mac_addr)
    
    if (status2) {
        log.info "present"
        sendEvent(name: "presence", value: "present")
    } else {
        log.info "not present"
        sendEvent(name: "presence", value: "not present")
    }
    
    if (autoUpdate) runIn(600, Update)
}



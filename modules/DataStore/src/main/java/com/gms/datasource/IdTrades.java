package com.gms.datasource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.*;

/**
 * Created by gms on 7/16/2017.
 */
@JsonSerialize
public class IdTrades {

    @JsonProperty("ListOfTradeIds")
    public List<IdTrade> idTrades;

    public IdTrades(List<IdTrade> idTrades) {
        this.idTrades = idTrades;
    }

    public IdTrades(){
        idTrades = new ArrayList<>();
    }
    public List<IdTrade> getIdTrades() {
        return idTrades;
    }

    public void mapToList(Map<String, IdTrade> idTrades) {
        this.idTrades = new ArrayList<>();
        for ( Map.Entry<String, IdTrade> it : idTrades.entrySet() ){
            this.idTrades.add(it.getValue());
        }
    }

    public void setIdTrades(List<IdTrade> idTrades) {
        this.idTrades = idTrades;
    }

    public boolean add(IdTrade idTrade){
        return idTrades.add(idTrade);
    }

    public IdTrade get(String key) {

        for ( IdTrade idTrade : idTrades ){
            if ( idTrade.getId().equals(key))
                return idTrade;
        }
        return new IdTrade();
    }

}

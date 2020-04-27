package com.example.finderfood;

import android.os.Parcel;
import android.os.Parcelable;

public class UserItens implements Parcelable {

    //id do usuario
    private String uuid;
    //titulo da receita
    private String tituloReceita;
    //tipo da receita
    private String tipoReceita;
    //descricao da receita
    private String descricaoReceita;
    //caminho da foto
    private String profileUrl;

    public static final Creator<UserItens> CREATOR = new Creator<UserItens>() {
        @Override
        public UserItens createFromParcel(Parcel in) {
            return new UserItens(in);
        }

        @Override
        public UserItens[] newArray(int size) {
            return new UserItens[size];
        }
    };

    public String getUuid() {
        return uuid;
    }

    public String getTituloReceita() {
        return tituloReceita;
    }

    public String getTipoReceita() {
        return tipoReceita;
    }

    public String getDescricaoReceita() {
        return descricaoReceita;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public UserItens() {

    }

    public UserItens(String uuid, String tituloReceita, String tipoReceita, String descricaoReceita, String profileUrl) {
        this.uuid = uuid;
        this.tituloReceita = tituloReceita;
        this.tipoReceita = tipoReceita;
        this.descricaoReceita = descricaoReceita;
        this.profileUrl = profileUrl;
    }

    protected UserItens(Parcel in) {
        uuid = in.readString();
        tituloReceita = in.readString();
        tipoReceita = in.readString();
        descricaoReceita = in.readString();
        profileUrl = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setTituloReceita(String tituloReceita) {
        this.tituloReceita = tituloReceita;
    }

    public void setTipoReceita(String tipoReceita) {
        this.tipoReceita = tipoReceita;
    }

    public void setDescricaoReceita(String descricaoReceita) {
        this.descricaoReceita = descricaoReceita;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeString(tituloReceita);
        dest.writeString(tipoReceita);
        dest.writeString(descricaoReceita);
        dest.writeString(profileUrl);
    }
}

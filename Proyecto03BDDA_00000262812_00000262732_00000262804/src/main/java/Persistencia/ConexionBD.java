/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import Interfaces.IConexionBD;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 *
 * @author BALAMRUSH
 */
public class ConexionBD implements IConexionBD{

    @Override
    public MongoDatabase conexion() {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry =fromRegistries( MongoClientSettings.getDefaultCodecRegistry(),pojoCodecRegistry);
        ConnectionString cadenaConexion =new ConnectionString("mongodb://localhost:27017");
        MongoClientSettings settings =MongoClientSettings.builder().applyConnectionString(cadenaConexion).codecRegistry(codecRegistry).build();
        MongoClient cliente = MongoClients.create(settings);
        return cliente.getDatabase("bibliotecaMusical02");
    }  
}

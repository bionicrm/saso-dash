package io.saso.dash.config;

import com.google.gson.Gson;

public final class ConfigModel
{
    private static final Gson gson = new Gson();

    public Server server = new Server();
    public DB db = new DB();
    public Redis redis = new Redis();
    public Cache cache = new Cache();
    public Limits limits = new Limits();

    public final class Server {
        public String wsUrl = "ws://127.0.0.1";
        public Bind bind = new Bind();

        public class Bind {
            public String host = "127.0.0.1";
            public int port = 80;
        }
    }

    public final class DB {
        public String host = "127.0.0.1";
        public int port = 5432;
        public String database = "postgres";
        public String user = "postgres";
        public String password = "";
    }

    public final class Redis {
        public String host = "127.0.0.1";
        public int port = 6379;
        public String password = "";
    }

    public final class Cache {
        public boolean templates = false;
    }

    public final class Limits {
        public int connectionsPerUser = 3;
    }

    /**
     * Gets a JSON representation of this model.
     *
     * @return this model in JSON form
     */
    public String toJSON()
    {
        return gson.toJson(this);
    }

    /**
     * @see #toJSON()
     */
    @Override
    public String toString()
    {
        return toJSON();
    }
}

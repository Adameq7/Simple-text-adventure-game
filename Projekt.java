package projekt;

import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

class Character
{
    int X, Y;
    String Name;
    int Type;
    int Score;
    int HP;
    int Accuracy;
    int Equipped;
    boolean Moved;
    int Faction;
    Vector<Item> Inventory = new Vector<>();
    
    public boolean Attack(Room R, Item Weapon, Character Target)
    {
        Random random = new Random();
        
        int X = random.nextInt(100);

        if(Faction == 1 && Target.Faction == 2)
        {
            for(int i = 0; i < R.Characters.size(); i++)
            {
                if(R.Characters.get(i).Faction == 2)
                    R.Characters.get(i).Faction = 0;
            }
            System.out.println("Your attack has made the " + Target.toString() + " and its companions hostile.");
        }
        
        if(Weapon.Damage == 0)
            Weapon.Damage = 1;
        
        if(X < Accuracy)
        {
            boolean B = false;
            
            for(int i = 0; i < R.Characters.size(); i++)
            {
                if(R.Characters.get(i).toString().equals("player"))
                {
                    B = true;
                    break;
                }
            }
            
            if(B)
            {
                if(!Weapon.isSilver && Target.Type == 2)
                    System.out.println("A silver weapon is required to harm a ghost.");
                else
                {
                    System.out.println("The " + toString() + " hits the " + Target + (!Weapon.Name.equals("") ? " with " + Weapon.Name + " " : " ") + "for " + Weapon.Damage + " HP.");
                    Target.HP -= Weapon.Damage;
                }
            }
            else
            {
                System.out.println("You hear fighting echo through the dungeon.");
            }
        }
        else
            System.out.println("The " + toString() + " tries to hit the " + Target + ", but misses.");
        
        if(Target.HP <= 0)
        {
            Target.Die(R, this);
        }
        
        if(X < Accuracy)
            return true;
        else
            return false;
    }

    public boolean Throw(Room R, Item Weapon, Character Target1, Character Target2)
    {
        Random random = new Random();
        
        int X = random.nextInt(100);
        
        if(Target1 == null)
        {
            if(Weapon.Damage == 0)
                Weapon.Damage = 1;
            
            if(X < Accuracy)
            {
                if(!Weapon.isSilver && Target2.Type == 2)
                    System.out.println("A silver weapon is required to harm a ghost.");
                else
                {
                    System.out.println("The " + Weapon + " hits the " + Target2 + " for " + Weapon.Damage * 2 + " HP.");
                    Target2.HP -= Weapon.Damage * 2;
                }
                
                R.Items.add(Weapon);
                Inventory.removeElementAt(Inventory.indexOf(Weapon));
            }
            else
                System.out.println("The " + Weapon + " misses the " + Target2 + ".");

            if(Target2.HP <= 0)
            {
                Target2.Die(R, this);
            }
        }
        else if(Weapon == null)
        {
            if(X < Accuracy)
            {
                if(Target2.Type == 2)
                    System.out.println("A ghost cannot be harmed this way.");
                else
                {
                    System.out.println("The " + toString() + " throws the " + Target1 + " at the " + Target2 + ", damaging them both for 1 HP.");
                    Target1.HP -= 1;
                    Target2.HP -= 1;
                }
            }
            else
                System.out.println("The " + toString() + " throws the " + Target1 + " at the " + Target2 + ", but misses.");

            if(Target1.HP <= 0)
            {
                Target1.Die(R, this);
            }                        
            
            if(Target2.HP <= 0)
            {
                Target2.Die(R, this);
            }            
        }
        
        if(X < Accuracy)
            return true;
        else
            return false;
    }
    
    public void Die(Room R, Character C)
    {
                System.out.println("The " + toString() + " has been slain. The " + C + " gains " + Score + " points.\n" + (Inventory.size() > 0 ? "Following items have been dropped:" : ""));
                
                for(int i = 0; i < Inventory.size(); i++)
                {
                    R.Items.add(Inventory.get(i));
                    System.out.println(Inventory.get(i));
                }

                C.Score += Score;

                R.Characters.removeElementAt(R.Characters.indexOf(this));        
    }
    
    public void ShowEquipment()
    {
        if(Inventory.size() > 0)
        {
            System.out.println("Inventory:");

            for(int i = 0; i < Inventory.size(); i++)
            {
                System.out.println((i + 1) + ". " + Inventory.get(i));
            }
        }
        else
        {
            System.out.println("The inventory is empty.");
        }
    }
    
    public String toStringDetailed()
    {
        return "This is a " + toString() + ". It has " + HP + "HP." + (Equipped != -1 ? " It is wielding a " + Inventory.get(Equipped) + "." : "");
    }
    
    public String toString()
    {
        String s = "";
                        
            switch(Type)
            {
                case 0:
                    s += "player";
                break;
                case 1:
                    s += "rat";
                break;
                case 2:
                    s += "ghost";
                break;
                case 3:
                    s += "goblin";
                break;
                case 4:
                    s += "orc";
                break;
                case 5:
                    s += "dwarf";
                break;
            }
        
        return s;
    }
}

class Door
{
    boolean Locked;
    Room R1, R2;
    String KeyType;
    
    Door(Room R1, Room R2, boolean Locked, String KeyType)
    {
        this.R1 = R1;
        this.R2 = R2;
        this.Locked = Locked;
        this.KeyType = KeyType;
    }
    
    Door(Room R1, Room R2, boolean Locked)
    {
        this.R1 = R1;
        this.R2 = R2;
        this.Locked = Locked;
    }
    
    public String toString()
    {
        return "This door connects rooms (" + R1.X + ", " + R1.Y + ") and (" + R2.X + ", " + R2.Y + ")" + (Locked ? (" and is locked with " + KeyType + ".") : ".");
    }
}

class Item
{
    String Name;
    int Damage;
    int Rarity;
    boolean isSilver;

    Item(String Name)
    {
        this.Name = Name;
    }

    Item(String Name, int Damage)
    {
        this.Name = Name;
        this.Damage = Damage;
    }

    Item(String Name, int Damage, boolean isSilver)
    {
        this.Name = Name;
        this.Damage = Damage;
        this.isSilver = isSilver;
    }
    
    Item(int Rarity, String Name)
    {
        this.Name = Name;
        this.Rarity = Rarity;
    }

    Item(int Rarity, String Name, int Damage)
    {
        this.Name = Name;
        this.Damage = Damage;
        this.Rarity = Rarity;
    }

    Item(int Rarity, String Name, int Damage, boolean isSilver)
    {
        this.Name = Name;
        this.Damage = Damage;
        this.isSilver = isSilver;
        this.Rarity = Rarity;
    }
    
    boolean Use(Door D)
    {
        if(Name.equals(D.KeyType) && D.Locked)
        {
            D.Locked = false;
            return true;
        }
        else
        {
            return false;
        }
    }
    
    int Use(Character C)
    {
        switch(Name)
        {
            case "bread":
                C.HP += 5;
                System.out.println("Bread restores 5HP to " + C + ".");
                return 1;
            case "water":
                C.HP += 3;
                System.out.println("Water restores 3HP to " + C + ".");
                return 1;
            default:
                return -1;
        }
    }    

    public String toStringDetailed()
    {     
        return "This is a " + Name + ".";
    }
    
    public String toString()
    {     
        return Name;
    }    
}

class Room
{
    int X, Y;
    boolean Visited;
    int Path;
    Vector <Character> Characters = new Vector<>();
    Vector <Item> Items = new Vector<>();
    Vector <Door> Doors = new Vector<>();
    
    Room(int X, int Y)
    {
        this.X = X;
        this.Y = Y;
        Path = 1000000;
    }
    
    public String toString()
    {
        String s;

        s = "The room at (" + X + ", " + Y + ") contains " + Doors.size() + " door" + (Doors.size() != 1 ? "s, " : ", ") + Characters.size() + " character" + (Characters.size() != 1 ? "s, " : ", ") + "and " + Items.size() + " item" + (Items.size() != 1 ? "s" : "") + ".\n";
       
        if(Doors.size() > 0)
            s += "Doors:\n";
        for(int i = 0; i < Doors.size(); i++)
            s += (i + 1) + ". Door " + (i + 1) + (Doors.get(i).Locked ? " is locked and" : "") + " leads to: (" + ((this == Doors.get(i).R1) ? Doors.get(i).R2.X : Doors.get(i).R1.X) + ", " + ((this == Doors.get(i).R1) ? Doors.get(i).R2.Y : Doors.get(i).R1.Y) + ")\n";

        if(Characters.size() > 0)
            s += "Characters:\n";
        for(int i = 0; i < Characters.size(); i++)
            s += (i + 1) + ". " + Characters.get(i) + "\n";

        if(Items.size() > 0)
            s += "Items:\n";
        for(int i = 0; i < Items.size(); i++)
            s += (i + 1) + ". " + Items.get(i) + "\n";
        
        return s;
    }
}

class Pair
{
    public int First;
    public int Second;
    
    Pair(int X, int Y)
    {
        First = X;
        Second = Y;
    }
    
    public String toString()
    {
        return First + " " + Second;
    }
}

class Triple
{
    public int First;
    public int Second;
    public int Third;
    
    Triple(int X, int Y, int Z)
    {
        First = X;
        Second = Y;
        Third = Z;
    }
    
    public String toString()
    {
        return First + " " + Second + " " + Third;
    }
}

class Map
{
    int SizeX, SizeY;
    Room[][] MapData;
    int PlayerX, PlayerY;
    int MaxPoints;
    int TotalRarity;
    
    void processEnemies()
    {
        for(int i = 0; i < SizeY; i++)
        {
            for(int j = 0; j < SizeX; j++)
            {
                if(MapData[j][i] != null)
                {
                    for(int k = 0; k < MapData[j][i].Characters.size(); k++)
                    {
                        MapData[j][i].Characters.get(k).Moved = false;
                    }
                }
            }
        }
        for(int i = 0; i < SizeY; i++)
        {
            for(int j = 0; j < SizeX; j++)
            {
                if(MapData[j][i] != null)
                {
                    Random random = new Random();

                    boolean B = false;
                    int Bp = -1;
                    
                    int s = MapData[j][i].Characters.size();
                    
                    for(int k = 0; k < MapData[j][i].Characters.size(); k++)
                    {
//                        System.out.println(j + " " + i + " " + MapData[j][i].Characters.size());
                        if(MapData[j][i].Characters.get(k).Faction == 2)
                        {
                            for(int p = 0; p < MapData[j][i].Characters.size(); p++)
                            {
                                if(MapData[j][i].Characters.get(p).Faction == 0)
                                {
                                    B = true;
                                    Bp = p;
                                }
                            }

                            if(B)
                            {
                                try
                                {
                                    MapData[j][i].Characters.get(k).Attack(MapData[j][i], (MapData[j][i].Characters.get(k).Equipped != -1 ? MapData[j][i].Characters.get(k).Inventory.get(MapData[j][i].Characters.get(k).Equipped) : new Item("", 1)), MapData[j][i].Characters.get(Bp));                                
                                }
                                catch(IndexOutOfBoundsException e)
                                {

                                }
                            }
                            else
                            {
                                int x = random.nextInt(100);

                                if(x < 20)
                                {
                                    int y = random.nextInt(MapData[j][i].Doors.size());
                                    int TX = -1, TY = -1;

                                    if(MapData[j][i].Doors.get(y).R1 == MapData[j][i])
                                    {
                                        TX = MapData[j][i].Doors.get(y).R2.X;
                                        TY = MapData[j][i].Doors.get(y).R2.Y;
                                    }
                                    else
                                    {
                                        TX = MapData[j][i].Doors.get(y).R1.X;
                                        TY = MapData[j][i].Doors.get(y).R1.Y;                                
                                    }

                                    if(TX != -1 && !MapData[j][i].Doors.get(y).Locked)
                                    {
                                        MapData[j][i].Characters.get(k).Moved = true;
                                        moveCharacter(j, i, MapData[j][i].Characters.get(k), TX, TY);
                                    }
                                }                                
                            }
                        }
                        
                        B = false;
                        Bp = -1;
                        
//                        System.out.println("index: " + k + " size: " + MapData[j][i].Characters.size());

                        try
                        {
                            if(MapData[j][i].Characters.get(k).Faction != 2 && !MapData[j][i].Characters.get(k).toString().equals("player") && !MapData[j][i].Characters.get(k).Moved)
                            {
                                for(int p = 0; p < MapData[j][i].Characters.size(); p++)
                                {
                                    if(MapData[j][i].Characters.get(p).toString().equals("player") || MapData[j][i].Characters.get(p).Faction == 2)
                                    {
                                        B = true;
                                        Bp = p;
                                        break;
                                    }
                                }

                                if(B)
                                {
                                    try
                                    {
                                        MapData[j][i].Characters.get(k).Attack(MapData[j][i], (MapData[j][i].Characters.get(k).Equipped != -1 ? MapData[j][i].Characters.get(k).Inventory.get(MapData[j][i].Characters.get(k).Equipped) : new Item("", 1)), MapData[j][i].Characters.get(Bp));                                
                                    }
                                    catch(IndexOutOfBoundsException e)
                                    {

                                    }
                                }
                                else
                                {
                                    int x = random.nextInt(100);

                                    if(x < 20)
                                    {
                                        int y = random.nextInt(MapData[j][i].Doors.size());
                                        int TX = -1, TY = -1;

                                        if(MapData[j][i].Doors.get(y).R1 == MapData[j][i])
                                        {
                                            TX = MapData[j][i].Doors.get(y).R2.X;
                                            TY = MapData[j][i].Doors.get(y).R2.Y;
                                        }
                                        else
                                        {
                                            TX = MapData[j][i].Doors.get(y).R1.X;
                                            TY = MapData[j][i].Doors.get(y).R1.Y;                                
                                        }

                                        if(TX != -1 && !MapData[j][i].Doors.get(y).Locked)
                                        {
                                            MapData[j][i].Characters.get(k).Moved = true;
                                            moveCharacter(j, i, MapData[j][i].Characters.get(k), TX, TY);
                                        }
                                    }                                
                                }
                            }
                        }
                        catch(IndexOutOfBoundsException e)
                        {

                        }
                    }
                }
            }
        }
    }
    
    Map(int SizeX, int SizeY, int C, int EnemyCount, int ItemCount, Vector <Item> ItemList)
    {
        for(int i = 0; i < ItemList.size(); i++)
            TotalRarity += ItemList.get(i).Rarity;
        
        this.SizeX = SizeX;
        this.SizeY = SizeY;
        
        MapData = new Room[SizeX][SizeY];
        
        int Counter = C;
        
        for(int i = 0; i < SizeY; i++)
            for(int j = 0; j < SizeX; j++)
                MapData[j][i] = new Room(j, i);

        for(int i = 0; i < SizeY; i++)
        {
            for(int j = 0; j < SizeX; j++)
            {
                if(j < SizeX - 1)
                {
                    Door D1 = new Door(MapData[j][i], MapData[j + 1][i], false);
                    MapData[j][i].Doors.add(D1);
                    MapData[j + 1][i].Doors.add(D1);
                }
                if(i < SizeY - 1)
                {
                    Door D2 = new Door(MapData[j][i], MapData[j][i + 1], false);
                    MapData[j][i].Doors.add(D2);
                    MapData[j][i + 1].Doors.add(D2);
                }
            }
        }
        
        Vector <Triple> V = new Vector<>();
        boolean[][] Visited = new boolean[SizeX][SizeY];
        boolean B = true;
        Random random = new Random();
        Door DB = new Door(null, null, false);
        
        while(B)
        {
            int X = random.nextInt(SizeX);
            int Y = random.nextInt(SizeY);
            int Z = 0;

            if(MapData[X][Y].Doors.size() > 1)
            {
                Z = random.nextInt(MapData[X][Y].Doors.size());
                
                DB = MapData[X][Y].Doors.get(Z);
                
                if((MapData[X][Y].Doors.get(Z).R1.X == X) && (MapData[X][Y].Doors.get(Z).R1.Y == Y))
                {
                    MapData[X][Y].Doors.get(Z).R2.Doors.remove(MapData[X][Y].Doors.get(Z));
                    MapData[X][Y].Doors.remove(MapData[X][Y].Doors.get(Z));
                }
                else
                {
                    MapData[X][Y].Doors.get(Z).R1.Doors.remove(MapData[X][Y].Doors.get(Z));
                    MapData[X][Y].Doors.remove(MapData[X][Y].Doors.get(Z));                   
                }
            }
            
            for(int i = 0; i < SizeY; i++)
                for(int j = 0; j < SizeX; j++)
                    Visited[j][i] = false;

            V.add(new Triple(0, 0, 0));

            while(V.size() > 0)
            {
                Triple P = new Triple(V.get(0).First, V.get(0).Second, V.get(0).Third);
                
                Visited[P.First][P.Second] = true;
                MapData[P.First][P.Second].Path = P.Third;                
                
                for(int i = 0; i < MapData[P.First][P.Second].Doors.size(); i++)
                {
                    Door D = MapData[P.First][P.Second].Doors.get(i);
                    int TX = 0;
                    int TY = 0;

                    if((D.R1.X == P.First) && (D.R1.Y == P.Second))
                    {
                        TX = D.R2.X;
                        TY = D.R2.Y;
                    }
                    else
                    {
                        TX = D.R1.X;
                        TY = D.R1.Y;
                    }
                    
                    if(Visited[TX][TY] && (MapData[TX][TY].Path > P.Third + 1))
                        Visited[TX][TY] = false;
                    
                    if(!Visited[TX][TY])
                    {
                        V.add(new Triple(TX, TY, P.Third + 1));
                        MapData[TX][TY].Path = P.Third + 1;
                    }
                }

                V.removeElementAt(0);
            }
            
            if(!Visited[SizeX - 1][SizeY - 1])
            {
                MapData[X][Y].Doors.add(DB);

                if((MapData[X][Y].Doors.get(MapData[X][Y].Doors.size() - 1).R1.X == X) && (MapData[X][Y].Doors.get(MapData[X][Y].Doors.size() - 1).R1.Y == Y))
                {
                    MapData[MapData[X][Y].Doors.get(MapData[X][Y].Doors.size() - 1).R2.X][MapData[X][Y].Doors.get(MapData[X][Y].Doors.size() - 1).R2.Y].Doors.add(DB);
                }
                else
                {
                    MapData[MapData[X][Y].Doors.get(MapData[X][Y].Doors.size() - 1).R1.X][MapData[X][Y].Doors.get(MapData[X][Y].Doors.size() - 1).R1.Y].Doors.add(DB);                    
                }
                
                Visited[SizeX - 1][SizeY - 1] = true;
                
                Counter--;
            }
            
            if(Counter == 0)
                B = false;
            
            if(B == false)
            {
                for(int i = 0; i < SizeY; i++)
                    for(int j = 0; j < SizeX; j++)
                        Visited[j][i] = false;
                
                V.add(new Triple(0, 0, 0));
                
                while(V.size() > 0)
                {
                    Triple P = new Triple(V.get(0).First, V.get(0).Second, V.get(0).Third);

                    Visited[P.First][P.Second] = true;
                    MapData[P.First][P.Second].Path = P.Third;
                    
                    for(int i = 0; i < MapData[P.First][P.Second].Doors.size(); i++)
                    {
                        Door D = MapData[P.First][P.Second].Doors.get(i);
                        int TX = 0;
                        int TY = 0;

                        if((D.R1.X == P.First) && (D.R1.Y == P.Second))
                        {
                            TX = D.R2.X;
                            TY = D.R2.Y;
                        }
                        else
                        {
                            TX = D.R1.X;
                            TY = D.R1.Y;
                        }

                        if(Visited[TX][TY] && (MapData[TX][TY].Path > P.Third + 1))
                            Visited[TX][TY] = false;

                        if(!Visited[TX][TY])
                        {
                            V.add(new Triple(TX, TY, P.Third + 1));
                            MapData[TX][TY].Path = P.Third + 1;
                        }
                    }

                    V.removeElementAt(0);
                }

                for(int i = 0; i < SizeY; i++)
                {
                    for(int j = 0; j < SizeX; j++)
                    {
                        if(!Visited[j][i])
                            MapData[j][i] = null;
                    }
                }
            }
        }

            String CurrentKey = "blue key";
            
            B = true;
            
            while(B)
            {
                int X = random.nextInt(SizeX);
                int Y = random.nextInt(SizeY);

                int Path;
                
                if(!(X == 0 && Y == 0) && MapData[X][Y] != null)
                {
                    Path = MapData[X][Y].Path;
                    
                    for(int j = 0; j < MapData[X][Y].Doors.size(); j++)
                    {
                        MapData[X][Y].Doors.get(j).Locked = true;
                        MapData[X][Y].Doors.get(j).KeyType = CurrentKey;                        
                    }
                    
                    boolean B2 = true;
                    
                    while(B2)
                    {
                        X = random.nextInt(SizeX);
                        Y = random.nextInt(SizeY);
                        
                        if(MapData[X][Y] != null)
                        {
                            if(MapData[X][Y].Path < Path)
                            {
                                MapData[X][Y].Items.add(new Item(CurrentKey));
                                B2 = false;
                            }
                        }
                    }
                    
                    switch(CurrentKey)
                    {
                        case "blue key":
                            CurrentKey = "red key";
                        break;
                        case "red key":
                            CurrentKey = "green key";
                        break;
                        case "green key":
                            CurrentKey = "yellow key";
                        break;
                        case "yellow key":
                            B = false;
                        break;
                    }
                }
            }
            
            for(int i = 0; i < EnemyCount; i++)
            {
                Character enemy = new Character();
                int t = random.nextInt(5);
                
                switch(t)
                {
                    case 0:
                        enemy.Type = 1;
                        enemy.HP = 3;
                        enemy.Score = 15;
                        enemy.Accuracy = 50;
                        enemy.Equipped = -1;
                    break;
                    case 1:
                        enemy.Type = 2;
                        enemy.HP = 8;
                        enemy.Score = 40;
                        enemy.Accuracy = 33;
                        enemy.Equipped = -1;
                    break;
                    case 2:
                        enemy.Type = 3;
                        enemy.HP = 6;
                        enemy.Score = 30;
                        enemy.Accuracy = 40;
                        enemy.Equipped = 0;
                        
                        int x = random.nextInt(2);
                        if(x == 0)
                            enemy.Inventory.add(ItemList.get(2));
                        else
                            enemy.Inventory.add(ItemList.get(3));
                        
                        int y = random.nextInt(2);
                        if(y == 0)
                            enemy.Inventory.add(ItemList.get(0));
                        else
                            enemy.Inventory.add(ItemList.get(1));
                    break;
                    case 3:
                        enemy.Type = 4;
                        enemy.HP = 12;
                        enemy.Score = 60;
                        enemy.Accuracy = 50;
                        enemy.Equipped = 0;
                        x = random.nextInt(2);
                        
                        if(x == 0)
                            enemy.Inventory.add(ItemList.get(3));
                        else
                            enemy.Inventory.add(ItemList.get(4));
                        
                        enemy.Inventory.add(ItemList.get(0));
                        enemy.Inventory.add(ItemList.get(1));                        
                    break;                    
                    case 4:
                        enemy.Type = 5;
                        enemy.HP = 10;
                        enemy.Score = 50;
                        enemy.Accuracy = 66;
                        enemy.Equipped = 0;
                        enemy.Faction = 2;
                        x = random.nextInt(2);
                        
                        if(x == 0)
                            enemy.Inventory.add(ItemList.get(3));
                        else
                            enemy.Inventory.add(ItemList.get(6));
                        
                        enemy.Inventory.add(ItemList.get(0));
                        enemy.Inventory.add(ItemList.get(1));
                    break;                    
                }
                
                B = true;
                
                while(B)
                {
                    int X = random.nextInt(SizeX);
                    int Y = random.nextInt(SizeY);

                    if(MapData[X][Y] != null)
                    {
                        MapData[X][Y].Characters.add(enemy);
                        MaxPoints += enemy.Score;
                        B = false;
                    }
                }
            }
            
            for(int i = 0; i < ItemCount; i++)
            {
                int t = random.nextInt(TotalRarity);
                Item item = new Item("");

                int x = 0;
                
                for(int j = 0; j < ItemList.size(); j++)
                {
                    if(x < t && t <= x + ItemList.get(j).Rarity)
                    {
                        item = ItemList.get(j);
                        break;
                    }
                    
                    x += ItemList.get(j).Rarity;
                }
                                
                B = true;
                
                while(B)
                {
                    int X = random.nextInt(SizeX);
                    int Y = random.nextInt(SizeY);

                    if(MapData[X][Y] != null)
                    {
                        MapData[X][Y].Items.add(item);
                        B = false;
                    }
                }
            }

    }
    
    public void moveCharacter(int X1, int Y1, Character Char, int X2, int Y2)
    {
        int ID = MapData[X1][Y1].Characters.indexOf(Char);
        MapData[X2][Y2].Characters.add(MapData[X1][Y1].Characters.get(ID));
        MapData[X1][Y1].Characters.removeElementAt(ID);
    }
    
    public String toString()
    {
        String s = "";
        
        for(int i = 0; i < SizeY; i++)
        {
            for(int j = 0; j < SizeX; j++)
            {
                if(MapData[j][i] != null && MapData[j][i].Visited)
                {
                    boolean[] B = new boolean[] {false, false, false, false};

                    for(int k = 0; k < MapData[j][i].Doors.size(); k++)
                    {
                        if(MapData[j][i].Doors.get(k).R1.X < j)
                            B[3] = true;
                        if(MapData[j][i].Doors.get(k).R2.X < j)
                            B[3] = true;
                        if(MapData[j][i].Doors.get(k).R1.X > j)
                            B[1] = true;
                        if(MapData[j][i].Doors.get(k).R2.X > j)
                            B[1] = true;
                        if(MapData[j][i].Doors.get(k).R1.Y < i)
                            B[0] = true;
                        if(MapData[j][i].Doors.get(k).R2.Y < i)
                            B[0] = true;
                        if(MapData[j][i].Doors.get(k).R1.Y > i)
                            B[2] = true;
                        if(MapData[j][i].Doors.get(k).R2.Y > i)
                            B[2] = true;
                    }

                    char C = '░';

                    if(B[0] && !B[1] && !B[2] && !B[3])
                        C = '║';
                    if(!B[0] && B[1] && !B[2] && !B[3])
                        C = '═';
                    if(!B[0] && !B[1] && B[2] && !B[3])
                        C = '║';
                    if(!B[0] && !B[1] && !B[2] && B[3])
                        C = '═';
                    if(B[0] && B[1] && !B[2] && !B[3])
                        C = '╚';
                    if(!B[0] && B[1] && B[2] && !B[3])
                        C = '╔';
                    if(!B[0] && !B[1] && B[2] && B[3])
                        C = '╗';
                    if(B[0] && !B[1] && !B[2] && B[3])
                        C = '╝';
                    if(B[0] && !B[1] && B[2] && !B[3])
                        C = '║';
                    if(!B[0] && B[1] && !B[2] && B[3])
                        C = '═';
                    if(B[0] && B[1] && B[2] && !B[3])
                        C = '╠';
                    if(!B[0] && B[1] && B[2] && B[3])
                        C = '╦';
                    if(B[0] && !B[1] && B[2] && B[3])
                        C = '╣';
                    if(B[0] && B[1] && !B[2] && B[3])
                        C = '╩';
                    if(B[0] && B[1] && B[2] && B[3])
                        C = '╬';

                    if(j == PlayerX && i == PlayerY)
                        C = '┼';
                    
                    s += C;
                }
                else
                {
                    s += '░';
                }
                
//                s += ' ';
            }

            s += "\n";
        }
        
        s += "┼ - player\n";
        
        return s;
    }
}

public class Projekt
{
    public static void main(String[] args) 
    {
        Scanner input = new Scanner(System.in);
//        String s = input.nextLine();
//        System.out.println(s);
        System.out.println("Loading...");
        
        Vector <Item> ItemList = new Vector<>();
        ItemList.add(new Item(4, "bread"));
        ItemList.add(new Item(5, "water"));
        ItemList.add(new Item(1, "iron dagger", 2));
        ItemList.add(new Item(3, "iron axe", 3));
        ItemList.add(new Item(1, "iron sword", 4));
        ItemList.add(new Item(3, "silver dagger", 3, true));
        ItemList.add(new Item(2, "silver axe", 4, true));
        ItemList.add(new Item(1, "silver sword", 5, true));
        ItemList.add(new Item(4, "treasure"));

        int a, b, c, d, f;
        
        if(args.length >= 5)
        {
            a = Integer.parseInt(args[0]);
            b = Integer.parseInt(args[1]);
            c = Integer.parseInt(args[2]);
            d = Integer.parseInt(args[3]);
            f = Integer.parseInt(args[4]);
        }
        else
        {
            a = 9;
            b = 9;
            c = 10;
            d = 16;
            f = 24;
        }
        
        Map map = new Map(a, b, c, d, f, ItemList);

        Character player = new Character();
        Character rat = new Character();
        player.Type = 0;
        player.X = 0;
        player.Y = 0;
        player.HP = 15;
        player.Accuracy = 80;
        player.Equipped = -1;
        player.Faction = 1;
        player.Inventory.add(ItemList.get(2));
        player.Inventory.add(ItemList.get(0));
        
        map.MapData[0][0].Characters.add(player);      
        map.MapData[0][0].Visited = true;
        
        String Input;
        
        boolean RunGame = true;

        String NotRecognized = "Command not recognized. Type \"help\" for help.";
        String Help = "Commandlist:\n" + 
                      "look {around | at {character <character_id> | item <item_id> | door <door_id> | <character_name> | <item_name>}}\n" +
                      "go {north | east | south | east | to {<door_id>}}\n" + 
                      "map\n" + 
                      "inventory\n" + 
                      "use {<inventory_item_name> | <inventory_item_id>} on {door <door_id> | character <character_id> | <character_name>}\n" +
                      "take {<item_name> | item <item_id>}\n" +
                      "equip {<inventory_item_name> | <inventory_item_id>}\n" + 
                      "attack {{<character_name> | <character_id>} | with {<invetory_item_id> | <inventory_item_name>}}\n" +
                      "throw {<character_name> | character <character_id> | inventory <inventory_item_id> | <inventory_item_name>} at {<character_name> | character <character_id>}";
        
        boolean CorrectCommand;
        boolean PlayerMoved;

        System.out.println("...Ready.");
        System.out.println(map.MapData[0][0]);
        
        while(RunGame)
        {
            Input = input.nextLine();
            String lower = Input.toLowerCase();
            String[] parts = lower.split(" ");
            CorrectCommand = false;
            PlayerMoved = false;
            
            if(parts.length > 0)
                switch(parts[0])
                {
                    case "map":
                        System.out.println(map);
                        CorrectCommand = true;
                    break;
                    case "take":
                        if(parts.length > 1)
                        {
                            int PartOffset = 0;
                            
                            int ID1 = -1;
                            
                            while(ID1 == -1 && PartOffset < parts.length - 1)
                            {
                                for(int i = map.MapData[player.X][player.Y].Items.size() - 1; i >= 0; i--)
                                {
                                    if(parts[1].equals(map.MapData[player.X][player.Y].Items.get(i).toString()))
                                        ID1 = i;
                                }
                                
                                if(ID1 == -1)
                                {
                                    if(2 + PartOffset < parts.length)                                    
                                        parts[1] += " " + parts[2 + PartOffset];
                                    PartOffset++;
                                }
                            }
                            
                            parts = lower.split(" ");
                            
                            boolean B = true;
                            
                            if(ID1 == -1)
                                switch(parts[1])
                                {
                                    case "item":
                                        if(parts.length > 2)
                                        {
                                            ID1 = -1;

                                            for(int i = map.MapData[player.X][player.Y].Items.size() - 1; i >= 0; i--)
                                            {
                                                if(parts[2].equals(map.MapData[player.X][player.Y].Items.get(i).toString()))
                                                    ID1 = i;
                                            }

                                            B = true;

                                            if(ID1 == -1)
                                                try
                                                {
                                                    ID1 = Integer.parseInt(parts[2]) - 1;                                    
                                                }
                                                catch(NumberFormatException e)
                                                {
                                                    B = false;
                                                }

                                            if(ID1 < map.MapData[player.X][player.Y].Items.size() && B && ID1 >= 0)
                                            {
                                                System.out.println("Item " + map.MapData[player.X][player.Y].Items.get(ID1) + " has been added to inventory.");
                                                player.Inventory.add(map.MapData[player.X][player.Y].Items.get(ID1));
                                                map.MapData[player.X][player.Y].Items.removeElementAt(ID1);
                                            }
                                            else
                                                System.out.println("Incorrect item ID.");

                                            CorrectCommand = true;
                                            PlayerMoved = true;
                                        }
                                    break;
                                }
                            else
                            {
                                if(!map.MapData[player.X][player.Y].Items.get(ID1).toString().equals("treasure"))
                                    System.out.println("Item " + map.MapData[player.X][player.Y].Items.get(ID1) + " has been added to inventory.");
                                else
                                {
                                    System.out.println("You are awarded 10 points for finding treasure.");
                                    player.Score += 10;
                                }
                                player.Inventory.add(map.MapData[player.X][player.Y].Items.get(ID1));
                                map.MapData[player.X][player.Y].Items.removeElementAt(ID1);
                                CorrectCommand = true;
                                PlayerMoved = true;
                            }
                        }
                    break;
                    case "look":
                        if(parts.length > 1)
                            switch(parts[1])
                            {
                                case "around":
                                    System.out.println(map.MapData[player.X][player.Y]);
                                    CorrectCommand = true;
                                break;
                                case "at":
                                    if(parts.length > 2)
                                    {
                                        int PartOffset = 0;
                                        int Type = -1;
                                        int ID1 = -1;

                                        while(ID1 == -1 && PartOffset < parts.length - 2)
                                        {
                                            for(int i = map.MapData[player.X][player.Y].Items.size() - 1; i >= 0; i--)
                                            {
                                                if(parts[2].equals(map.MapData[player.X][player.Y].Items.get(i).toString()))
                                                {
                                                    ID1 = i;
                                                    Type = 0;
                                                }
                                            }
                                            for(int i = map.MapData[player.X][player.Y].Characters.size() - 1; i >= 0; i--)
                                            {
                                                if(parts[2].equals(map.MapData[player.X][player.Y].Characters.get(i).toString()))
                                                {
                                                    ID1 = i;
                                                    Type = 1;
                                                }
                                            }
                                            
                                            if(ID1 == -1)
                                            {
                                                if(3 + PartOffset < parts.length)
                                                    parts[2] += " " + parts[3 + PartOffset];
                                                PartOffset++;
                                            }
                                        }

                                        parts = lower.split(" ");

                                        if(ID1 != -1)
                                        {
                                            if(Type == 0)
                                            {
                                                System.out.println(map.MapData[player.X][player.Y].Items.get(ID1).toStringDetailed());
                                            }
                                            else
                                            {
                                                System.out.println(map.MapData[player.X][player.Y].Characters.get(ID1).toStringDetailed());
                                            }
                                            
                                            CorrectCommand = true;
                                        }
                                        else
                                            switch(parts[2])
                                            {
                                                case "door":
                                                    if(parts.length > 3)
                                                    {
                                                        int ID = -1;

                                                        boolean B = true;

                                                        if(ID == -1)
                                                            try
                                                            {
                                                                ID = Integer.parseInt(parts[3]) - 1;                                    
                                                            }
                                                            catch(NumberFormatException e)
                                                            {
                                                                B = false;
                                                            }

                                                        if(ID < map.MapData[player.X][player.Y].Doors.size() && B && ID >= 0)
                                                        {
                                                            System.out.println(map.MapData[player.X][player.Y].Doors.get(ID));
                                                            CorrectCommand = true;
                                                        }
                                                        else
                                                        {
                                                            System.out.println("Incorrect door ID.");
                                                            CorrectCommand = true;
                                                        }                                                     
                                                    }                                                
                                                break;
                                                case "character":
                                                    if(parts.length > 3)
                                                    {
                                                        int ID = -1;

                                                        boolean B = true;

                                                        if(ID == -1)
                                                            try
                                                            {
                                                                ID = Integer.parseInt(parts[3]) - 1;                                    
                                                            }
                                                            catch(NumberFormatException e)
                                                            {
                                                                B = false;
                                                            }

                                                        if(ID < map.MapData[player.X][player.Y].Characters.size() && B && ID >= 0)
                                                        {
                                                            System.out.println(map.MapData[player.X][player.Y].Characters.get(ID).toStringDetailed());
                                                            CorrectCommand = true;
                                                        }
                                                        else
                                                        {
                                                            System.out.println("Incorrect character ID.");
                                                            CorrectCommand = true;
                                                        }                                                     
                                                    }
                                                break;
                                                case "item":
                                                    if(parts.length > 3)
                                                    {
                                                        int ID = -1;

                                                        boolean B = true;

                                                        if(ID == -1)
                                                            try
                                                            {
                                                                ID = Integer.parseInt(parts[3]) - 1;                                    
                                                            }
                                                            catch(NumberFormatException e)
                                                            {
                                                                B = false;
                                                            }

                                                        if(ID < map.MapData[player.X][player.Y].Items.size() && B && ID >= 0)
                                                        {
                                                            System.out.println(map.MapData[player.X][player.Y].Items.get(ID).toStringDetailed());
                                                            CorrectCommand = true;
                                                        }
                                                        else
                                                        {
                                                            System.out.println("Incorrect item ID.");
                                                            CorrectCommand = true;
                                                        }                                                     
                                                    }
                                                break;                                            
                                            }
                                    }
                                break;
                            }
                    break;
                    case "inventory":
                        player.ShowEquipment();
                        CorrectCommand = true;
                    break;
                    case "use":
                        if(parts.length > 1)
                        {
                            int PartOffset = 0;
                            
                            int ID1 = -1;
                            
                            while(ID1 == -1 && PartOffset < parts.length - 2)
                            {
                                for(int i = player.Inventory.size() - 1; i >= 0; i--)
                                {
                                    if(parts[1].equals(player.Inventory.get(i).toString()))
                                        ID1 = i;
                                }
                                
                                if(ID1 == -1)
                                {
                                    if(2 + PartOffset < parts.length)
                                        parts[1] += " " + parts[2 + PartOffset];
                                    PartOffset++;
                                }
                            }
                            
                            parts = lower.split(" ");
                            
                            boolean B = true;
                            
                            if(ID1 == -1)
                                try
                                {
                                    ID1 = Integer.parseInt(parts[2]) - 1; 
                                }
                                catch(NumberFormatException e)
                                {
                                    B = false;
                                }
                                finally
                                {
                                    PartOffset = 0;
                                }
                            else
                            {
                                
                            }
                            
                            if(ID1 < player.Inventory.size() && ID1 >= 0 && B)
                            {
                                if(parts.length > 2 + PartOffset)
                                {
                                    if(parts[2 + PartOffset].equals("on"))
                                    {
                                        if(parts.length > 3 + PartOffset)
                                        {
                                            int ID2 = -1, PartOffset2 = 0;
                                            
                                            B = true;

                                            while(ID2 == -1 && PartOffset2 < parts.length)
                                            {
                                                for(int i = map.MapData[player.X][player.Y].Characters.size() - 1; i >= 0; i--)
                                                {
                                                    if(parts[3 + PartOffset].equals(map.MapData[player.X][player.Y].Characters.get(i).toString()))
                                                        ID2 = i;
                                                }

                                                if(ID2 == -1)
                                                {
                                                    if(4 + PartOffset + PartOffset2 < parts.length)
                                                        parts[3 + PartOffset] += " " + parts[4 + PartOffset + PartOffset2];
                                                    PartOffset2++;
                                                }
                                            }

                                            parts = lower.split(" ");

                                            B = true;

                                            if(ID2 == -1)
                                                try
                                                {
                                                    ID1 = Integer.parseInt(parts[3 + PartOffset]) - 1; 
                                                }
                                                catch(NumberFormatException e)
                                                {
                                                    B = false;
                                                }
                                            
                                            if(ID2 != -1)
                                            {
                                                if(player.Inventory.get(ID1).Use(map.MapData[player.X][player.Y].Characters.get(ID2)) == 1)
                                                {
                                                    player.Inventory.removeElementAt(ID1);
                                                    if(player.Equipped > ID1)
                                                        player.Equipped--;
                                                }
                                                CorrectCommand = true;
                                            }
                                            else
                                                switch(parts[3 + PartOffset])
                                                {
                                                    case "door":
                                                        if(parts.length > 4 + PartOffset)
                                                        {
                                                            ID2 = -1;

                                                            B = true;

                                                            if(ID2 == -1)
                                                                try
                                                                {
                                                                    ID2 = Integer.parseInt(parts[4 + PartOffset]) - 1;                                    
                                                                }
                                                                catch(NumberFormatException e)
                                                                {
                                                                    B = false;
                                                                }

                                                            if(ID2 < map.MapData[player.X][player.Y].Doors.size()  && ID2 >= 0 && B)
                                                            {
                                                                if(player.Inventory.get(ID1).Use(map.MapData[player.X][player.Y].Doors.get(ID2)))
                                                                    System.out.println("Door " + (ID2 + 1) + " has been unlocked.");
                                                                else if(!map.MapData[player.X][player.Y].Doors.get(ID2).Locked)
                                                                    System.out.println("Door " + (ID2 + 1) + " is already open.");
                                                                else
                                                                    System.out.println("Door " + (ID2 + 1) + " has not been unlocked.");

                                                                CorrectCommand = true;
                                                                PlayerMoved = true;
                                                            }
                                                            else
                                                            {
                                                                System.out.println("Incorrect door ID.");
                                                                CorrectCommand = true;
                                                            }
                                                        }
                                                    break;
                                                }
                                        }
                                    }
                                }
                            }
                            else
                            {
                                System.out.println("Incorrect item ID.");
                                CorrectCommand = true;
                            }
                        }
                    break;
                    case "throw":
                        if(parts.length > 1)
                        {
                            int PartOffset = 0;
                            
                            int ID1 = -1, Type = -1;
                            
                            while(ID1 == -1 && PartOffset < parts.length - 2)
                            {
                                for(int i = player.Inventory.size() - 1; i >= 0; i--)
                                {
                                    if(parts[1].equals(player.Inventory.get(i).toString()))
                                    {
                                        ID1 = i;
                                        Type = 0;
                                    }
                                }

                                for(int i = map.MapData[player.X][player.Y].Characters.size() - 1; i >= 0; i--)
                                {                                
                                    if(parts[1].equals(map.MapData[player.X][player.Y].Characters.get(i).toString()))
                                    {
                                        ID1 = i;
                                        Type = 1;
                                    }
                                }
                                
                                if(ID1 == -1)
                                {
                                    if(2 + PartOffset < parts.length)
                                        parts[1] += " " + parts[2 + PartOffset];
                                    PartOffset++;
                                }
                            }
                            
                            parts = lower.split(" ");
                            
                            boolean B = true;
                            
                            if(ID1 == -1)
                                try
                                {
                                    ID1 = Integer.parseInt(parts[2]) - 1; 
                                }
                                catch(NumberFormatException e)
                                {
                                    B = false;
                                }
                                finally
                                {
                                    PartOffset = 0;
                                }
                            else
                            {
                                
                            }
                            
                            if(B)
                            {
                                if(parts[1].equals("inventory"))
                                {
                                    Type = 0;
                                    PartOffset++;
                                }
                                if(parts[1].equals("character"))
                                {
                                    Type = 1;
                                    PartOffset++;
                                }
                            }
                                                            
                            if(Type != -1 && ID1 >= 0 && B)
                            {
                                if(parts.length > 2 + PartOffset)
                                {
                                    if(parts[2 + PartOffset].equals("at"))
                                    {
                                        if(parts.length > 3 + PartOffset)
                                        {
                                            int ID2 = -1, PartOffset2 = 0;
                                            
                                            B = true;

                                            while(ID2 == -1 && PartOffset2 < parts.length)
                                            {
                                                for(int i = map.MapData[player.X][player.Y].Characters.size() - 1; i >= 0; i--)
                                                {
                                                    if(parts[3 + PartOffset].equals(map.MapData[player.X][player.Y].Characters.get(i).toString()))
                                                        ID2 = i;
                                                }

                                                if(ID2 == -1)
                                                {
                                                    if(4 + PartOffset + PartOffset2 < parts.length)
                                                        parts[3 + PartOffset] += " " + parts[4 + PartOffset + PartOffset2];
                                                    PartOffset2++;
                                                }
                                            }

                                            parts = lower.split(" ");

                                            B = true;

                                            if(ID2 == -1)
                                                try
                                                {
                                                    ID1 = Integer.parseInt(parts[3 + PartOffset]) - 1; 
                                                }
                                                catch(NumberFormatException e)
                                                {
                                                    B = false;
                                                }
                                            
                                            if(ID2 != -1)
                                            {
                                                switch(Type)
                                                {
                                                    case 1:
                                                        player.Throw(map.MapData[player.X][player.Y], null, map.MapData[player.X][player.Y].Characters.get(ID1), map.MapData[player.X][player.Y].Characters.get(ID2));
                                                    break;
                                                    case 0:
                                                        if(player.Throw(map.MapData[player.X][player.Y], player.Inventory.get(ID1), null, map.MapData[player.X][player.Y].Characters.get(ID2)))
                                                        {
                                                            if(player.Equipped == ID1)
                                                                player.Equipped = -1;
                                                            if(player.Equipped > ID1)
                                                                player.Equipped--;
                                                        }
                                                    break;
                                                }
                                                CorrectCommand = true;
                                                PlayerMoved = true;
                                            }
                                        }
                                    }
                                }
                            }
                            else
                            {
                                System.out.println("Incorrect item ID.");
                                CorrectCommand = true;
                            }
                        }
                    break;
                    case "equip":
                        if(parts.length > 1)
                        {
                            int PartOffset = 0;
                            
                            int ID1 = -1;
                            
                            while(ID1 == -1 && PartOffset < parts.length)
                            {
                                for(int i = player.Inventory.size() - 1; i >= 0; i--)
                                {
                                    if(parts[1].equals(player.Inventory.get(i).toString()))
                                        ID1 = i;
                                }
                                
                                if(ID1 == -1)
                                {
                                    if(2 + PartOffset < parts.length)
                                        parts[1] += " " + parts[2 + PartOffset];
                                    PartOffset++;
                                }
                            }
                            
                            parts = lower.split(" ");
                            
                            boolean B = true;
                            
                            if(ID1 == -1)
                                try
                                {
                                    ID1 = Integer.parseInt(parts[2]) - 1; 
                                }
                                catch(NumberFormatException e)
                                {
                                    B = false;
                                }
                                finally
                                {
                                    PartOffset = 0;
                                }
                            else
                            {
                                
                            }
                            
                            if(ID1 < player.Inventory.size() && ID1 >= 0 && B)
                            {
                                player.Equipped = ID1;
                                CorrectCommand = true;
                                PlayerMoved = true;
                                System.out.println("Item " + player.Inventory.get(ID1) + " equipped.");
                            }
                        }                        
                    break;
                    case "attack":
                        if(parts.length > 1)
                        {
                            int PartOffset = 0;
                            
                            int ID1 = -1;
                            
                            while(ID1 == -1 && PartOffset < parts.length)
                            {
                                for(int i = map.MapData[player.X][player.Y].Characters.size() - 1; i >= 0; i--)
                                {
                                    if(parts[1].equals(map.MapData[player.X][player.Y].Characters.get(i).toString()))
                                        ID1 = i;
                                }
                                
                                if(ID1 == -1)
                                {
                                    if(2 + PartOffset < parts.length - 1)
                                        parts[1] += " " + parts[2 + PartOffset];
                                    PartOffset++;
                                }
                            }
                            
                            parts = lower.split(" ");
                            
                            boolean B = true;
                            
                            if(ID1 == -1)
                            {
                                try
                                {
                                    ID1 = Integer.parseInt(parts[1]) - 1; 
                                }
                                catch(NumberFormatException e)
                                {
                                    B = false;
                                }
                                finally
                                {
                                    PartOffset = 0;
                                }
                            }
                            else if(PartOffset > 0)
                                PartOffset--;
                            
                            if(ID1 < map.MapData[player.X][player.Y].Characters.size() && ID1 >= 0 && B)
                            {
                                if(parts.length > 3 + PartOffset)
                                {
                                    if(parts[2 + PartOffset].equals("with"))
                                    {
                                        if(parts.length > 3 + PartOffset)
                                        {
                                            int ID2 = -1;                                        
                                            int PartOffset2 = 0;

                                            while(ID2 == -1 && PartOffset2 < parts.length)
                                            {                                                
                                                for(int i = player.Inventory.size() - 1; i >= 0; i--)
                                                {
                                                    if(parts[3 + PartOffset + PartOffset].equals(player.Inventory.get(i).toString()))
                                                        ID2 = i;
                                                }

                                                if(ID2 == -1)
                                                {
                                                    if(4 + PartOffset2 < parts.length)
                                                        parts[3 + PartOffset] += " " + parts[4 + PartOffset + PartOffset2];
                                                    PartOffset2++;
                                                }
                                            }

                                            parts = lower.split(" ");

                                            B = true;

                                            if(ID2 == -1)
                                                try
                                                {
                                                    ID2 = Integer.parseInt(parts[3 + PartOffset]) - 1; 
                                                }
                                                catch(NumberFormatException e)
                                                {
                                                    B = false;
                                                }
                                                finally
                                                {
                                                    PartOffset2 = 0;
                                                }
                                            else
                                                PartOffset2--;

                                            if(B)
                                                player.Attack(map.MapData[player.X][player.Y], player.Inventory.get(ID2), map.MapData[player.X][player.Y].Characters.get(ID1));
                                            CorrectCommand = true;
                                            PlayerMoved = true;
                                        }
                                    }
                                }
                                else
                                {
                                    if(B)
                                        if(player.Equipped != -1)
                                        {
                                            player.Attack(map.MapData[player.X][player.Y], player.Inventory.get(player.Equipped), map.MapData[player.X][player.Y].Characters.get(ID1));
                                            CorrectCommand = true;
                                            PlayerMoved = true;                                            
                                        }
                                    else
                                        {
                                            System.out.println("You have to equip a weapon first.");
                                            CorrectCommand = true;
                                        }
                                }
                            }
                            else
                            {
                                System.out.println("Incorrect character ID.");
                                CorrectCommand = true;
                            }
                        }
                    break;
                    case "go":
                        if(parts.length > 1)
                            switch(parts[1])
                            {
                                case "north":
                                    int TX = -1, TY = -1;
                                    
                                    for(int i = 0; i < map.MapData[player.X][player.Y].Doors.size(); i++)
                                    {
                                       if(map.MapData[player.X][player.Y].Doors.get(i).R1 == map.MapData[player.X][player.Y])
                                       {
                                           if(map.MapData[player.X][player.Y].Doors.get(i).R2.Y < player.Y)
                                           {
                                               TX = player.X;
                                               TY = player.Y - 1;
                                               player.Y--;
                                               
                                               break;
                                           }
                                       }
                                       else
                                       {
                                           if(map.MapData[player.X][player.Y].Doors.get(i).R1.Y < player.Y)
                                           {
                                               TX = player.X;
                                               TY = player.Y - 1;
                                               player.Y--;

                                               break;
                                           }
                                       }
                                    }
                                    
                                    if(TX == -1)
                                    {
                                        System.out.println("Can't go north.");                                        
                                    }
                                    else
                                    {
                                        map.moveCharacter(player.X, player.Y + 1, player, TX, TY);                                        
                                        System.out.println(map.MapData[player.X][player.Y]);
                                    }

                                    map.PlayerX = player.X;
                                    map.PlayerY = player.Y;
                                    map.MapData[player.X][player.Y].Visited = true;
                                    
                                    CorrectCommand = true;
                                    PlayerMoved = true;
                                break;
                                case "south":
                                    TX = -1; TY = -1;
                                    
                                    for(int i = 0; i < map.MapData[player.X][player.Y].Doors.size(); i++)
                                    {
                                       if(map.MapData[player.X][player.Y].Doors.get(i).R1 == map.MapData[player.X][player.Y])
                                       {
                                           if(map.MapData[player.X][player.Y].Doors.get(i).R2.Y > player.Y)
                                           {
                                               TX = player.X;
                                               TY = player.Y + 1;
                                               player.Y++;
                                           
                                               break;
                                           }
                                       }
                                       else
                                       {
                                           if(map.MapData[player.X][player.Y].Doors.get(i).R1.Y > player.Y)
                                           {
                                               TX = player.X;
                                               TY = player.Y + 1;
                                               player.Y++;
                                               
                                               break;
                                           }
                                       }
                                    }
                                    
                                    if(TX == -1)
                                    {
                                        System.out.println("Can't go south.");                                        
                                    }
                                    else
                                    {
                                        map.moveCharacter(player.X, player.Y - 1, player, TX, TY);                                        
                                        System.out.println(map.MapData[player.X][player.Y]);
                                    }
                                    
                                    map.PlayerX = player.X;
                                    map.PlayerY = player.Y;
                                    map.MapData[player.X][player.Y].Visited = true;
                                    
                                    CorrectCommand = true;
                                    PlayerMoved = true;
                                break;
                                case "east":
                                    TX = -1; TY = -1;
                                    
                                    for(int i = 0; i < map.MapData[player.X][player.Y].Doors.size(); i++)
                                    {
                                       if(map.MapData[player.X][player.Y].Doors.get(i).R1 == map.MapData[player.X][player.Y])
                                       {
                                           if(map.MapData[player.X][player.Y].Doors.get(i).R2.X > player.X)
                                           {
                                               TX = player.X + 1;
                                               TY = player.Y;
                                               player.X++;
                                               
                                               break;
                                           }
                                       }
                                       else
                                       {
                                           if(map.MapData[player.X][player.Y].Doors.get(i).R1.X > player.X)
                                           {
                                               TX = player.X + 1;
                                               TY = player.Y;
                                               player.X++;
                                               
                                               break;
                                           }
                                       }
                                    }
                                    
                                    if(TX == -1)
                                    {
                                        System.out.println("Can't go east.");                                        
                                    }
                                    else
                                    {
                                        map.moveCharacter(player.X - 1, player.Y, player, TX, TY);                                        
                                        System.out.println(map.MapData[player.X][player.Y]);
                                    }
                                    
                                    map.PlayerX = player.X;
                                    map.PlayerY = player.Y;
                                    map.MapData[player.X][player.Y].Visited = true;
                                    
                                    CorrectCommand = true;
                                    PlayerMoved = true;
                                break;
                                case "west":
                                    TX = -1; TY = -1;
                                    
                                    for(int i = 0; i < map.MapData[player.X][player.Y].Doors.size(); i++)
                                    {
                                       if(map.MapData[player.X][player.Y].Doors.get(i).R1 == map.MapData[player.X][player.Y])
                                       {
                                           if(map.MapData[player.X][player.Y].Doors.get(i).R2.X < player.X)
                                           {
                                               TX = player.X - 1;
                                               TY = player.Y;
                                               player.X--;
                                               
                                               break;
                                           }
                                       }
                                       else
                                       {
                                           if(map.MapData[player.X][player.Y].Doors.get(i).R1.X < player.X)
                                           {
                                               TX = player.X - 1;
                                               TY = player.Y;
                                               player.X--;
                                               
                                               break;
                                           }
                                       }
                                    }
                                    
                                    if(TX == -1)
                                    {
                                        System.out.println("Can't go west.");                                        
                                    }
                                    else
                                    {
                                        map.moveCharacter(player.X + 1, player.Y, player, TX, TY);                                        
                                        System.out.println(map.MapData[player.X][player.Y]);
                                    }
                                    
                                    map.PlayerX = player.X;
                                    map.PlayerY = player.Y;
                                    map.MapData[player.X][player.Y].Visited = true;
                                    
                                    CorrectCommand = true;
                                    PlayerMoved = true;                                    
                                break;
                                case "to":
                                    if(parts.length > 2)
                                    {
                                        int ID = -1;
                                        
                                        try
                                        {
                                            ID = Integer.parseInt(parts[2]) - 1;
                                        }
                                        catch(NumberFormatException e)
                                        {
//                                            System.out.println("Incorrect door ID.");
                                        }
                                        
                                        if(ID < map.MapData[player.X][player.Y].Doors.size() && ID >= 0)
                                        {
                                            if(!map.MapData[player.X][player.Y].Doors.get(ID).Locked)
                                            {
                                                int X, Y;

                                                if((map.MapData[player.X][player.Y].Doors.get(ID).R1.X == player.X) && (map.MapData[player.X][player.Y].Doors.get(ID).R1.Y == player.Y))
                                                {
                                                    X = map.MapData[player.X][player.Y].Doors.get(ID).R2.X;
                                                    Y = map.MapData[player.X][player.Y].Doors.get(ID).R2.Y;
                                                }
                                                else
                                                {
                                                    X = map.MapData[player.X][player.Y].Doors.get(ID).R1.X;
                                                    Y = map.MapData[player.X][player.Y].Doors.get(ID).R1.Y;
                                                }
                                                
                                                map.moveCharacter(player.X, player.Y, player, X, Y);
                                                player.X = X;
                                                player.Y = Y;
                                                map.PlayerX = player.X;
                                                map.PlayerY = player.Y;
                                                map.MapData[player.X][player.Y].Visited = true;

    //                                            System.out.println("Moved to (" + player.X + ", " + player.Y + ").");
                                                System.out.println(map.MapData[player.X][player.Y]);
                                                CorrectCommand = true;
                                                PlayerMoved = true;
                                            }
                                            else
                                            {
                                                System.out.println("The door is locked.");
                                                CorrectCommand = true;
                                            }
                                        }
                                        else
                                        {
                                            System.out.println("Incorrect door ID.");
                                            CorrectCommand = true;
                                        }
                                    }
                                break;
                            }
                    break;
                    case "exit":
                        RunGame = false;
                        CorrectCommand = true;
                    break;
                    case "help":
                        System.out.println(Help);
                        CorrectCommand = true;
                    break;
                }
            
            if(!CorrectCommand)
                System.out.println(NotRecognized);
            else if(PlayerMoved)
            {
                map.processEnemies();
            }
            
            if(player.HP <= 0)
            {
                System.out.println("You have lost with a score of a " + player.Score + " out of " + map.MaxPoints + ".");
                RunGame = false;
            }

            if(player.X == map.SizeX - 1 && player.Y == map.SizeY - 1)
            {
                System.out.println("You have won with a score of a " + player.Score + " out of " + map.MaxPoints + ".");
                RunGame = false;
            }
            
            System.out.print("\n");
        }
    }
}
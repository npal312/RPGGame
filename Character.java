public class Character{
    //superclass because I want some functionality to be inherited

    private String name;
    private int health;
    private int healthMax;
    private int attack;
    private int defense;
    private int stamina;
    private int staminaMax;
    private int speed;
    //maybe make profs only for player, not sure
    private int[] healthProf;
    private int[] attackProf;
    private int[] defenseProf;
    private int[] staminaProf;
    private int[] speedProf;

    private static final int healthCategory = 1; //health boost
    private static final int attackCategory = 7; //blunt, sharp, mystic, fire, electric, ice, poison
    private static final int defenseCategory = 7; //blunt, sharp, mystic, fire, electric, ice, poison
    private static final int staminaCategory = 2; //stamina bar, stamina recovery
    private static final int speedCategory = 2; //attack speed, priority speed

    //full constructor
    public Character(String name, int healthMax, int attack, int defense, int staminaMax, int speed, int[] healthProf, int[] attackProf, int[] defenseProf, int[] staminaProf, int[] speedProf){
        this.name = name;
        this.health = healthMax;
        this.healthMax = healthMax;
        this.attack = attack;
        this.defense = defense;
        this.stamina = staminaMax;
        this.staminaMax = staminaMax;
        this.speed = speed;
        this.healthProf = healthProf;
        this.attackProf = attackProf;
        this.defenseProf = defenseProf;
        this.staminaProf = staminaProf;
        this.speedProf = speedProf;
    }

    //partial constructor with only necessary values
    public Character(String name, int healthMax, int attack, int defense, int staminaMax, int speed){
        this.name = name;
        this.health = healthMax;
        this.healthMax = healthMax;
        this.attack = attack;
        this.defense = defense;
        this.stamina = staminaMax;
        this.staminaMax = staminaMax;
        this.speed = speed;
        this.healthProf = new int[healthCategory];
        this.attackProf = new int[attackCategory];
        this.defenseProf = new int[defenseCategory];
        this.staminaProf = new int[staminaCategory];
        this.speedProf = new int[speedCategory];
    }

    //empty constructor
    public Character(){
        this.name = "1";
        this.health = 1;
        this.healthMax = 1;
        this.attack = 1;
        this.defense = 1;
        this.stamina = 1;
        this.staminaMax = 1;
        this.speed = 1;
        this.healthProf = new int[healthCategory];
        this.attackProf = new int[attackCategory];
        this.defenseProf = new int[defenseCategory];
        this.staminaProf = new int[staminaCategory];
        this.speedProf = new int[speedCategory];
    }


    //methods for gaining and losing health and stamina
    //mainly used for combat
    public void loseHealth(int healthDrop){
        if (this.health - healthDrop < 0) {
            this.health = 0;
        }
        else {
            this.health -= healthDrop;
        }
    }

    public void gainHealth(int healthGain){
        if (this.health - healthGain > this.healthMax) {
            this.health = this.healthMax;
        }
        else {
            this.health += healthGain;
        }
    }

    public void loseStamina(int staminaDrop){
        if (this.stamina - staminaDrop < 0) {
            this.stamina = 0;
        }
        else {
            this.stamina -= staminaDrop;
        }
    }

    public void gainStamina(int staminaGain){
        if (this.stamina - staminaGain > this.healthMax) {
            this.stamina = this.staminaMax;
        }
        else {
            this.stamina += staminaGain;
        }
    }


    //Getter and Setter functions for all character stats
    public String getName() {
        return name;
    }

    //do later
    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        try{
            if (health < 0){
                throw new IllegalArgumentException("Health cannot be negative!");
            }
            this.health = health;
        }
        catch (IllegalArgumentException e){
            System.err.println("setHealth Error: " + e.getMessage());
        }
    }

    public int getHealthMax() {
        return healthMax;
    }

    public void setHealthMax(int healthMax) {
        try{
            if (healthMax <= 0){
                throw new IllegalArgumentException("Max Health cannot be negative or zero!");
            }
            this.healthMax = healthMax;
        }
        catch (IllegalArgumentException e){
            System.err.println("setHealthMax Error: " + e.getMessage());
        }
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        try{
            if (attack <= 0){
                throw new IllegalArgumentException("Attack cannot be negative or zero!");
            }
            this.attack = attack;
        }
        catch (IllegalArgumentException e){
            System.err.println("setAttack Error: " + e.getMessage());
        }
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        try{
            if (defense <= 0){
                throw new IllegalArgumentException("Defense cannot be negative or zero!");
            }
            this.defense = defense;
        }
        catch (IllegalArgumentException e){
            System.err.println("setDefense Error: " + e.getMessage());
        }
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        try{
            if (stamina < 0){
                throw new IllegalArgumentException("Stamina cannot be negative!");
            }
            this.stamina = stamina;
        }
        catch (IllegalArgumentException e){
            System.err.println("setStamina Error: " + e.getMessage());
        }
    }

    public int getStaminaMax() {
        return staminaMax;
    }

    public void setStaminaMax(int staminaMax) {
        try{
            if (staminaMax <= 0){
                throw new IllegalArgumentException("Max Stamina cannot be negative or zero!");
            }
            this.staminaMax = staminaMax;
        }
        catch (IllegalArgumentException e){
            System.err.println("setStaminaMax Error: " + e.getMessage());
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        try{
            if (speed <= 0){
                throw new IllegalArgumentException("Speed cannot be negative or zero!");
            }
            this.speed = speed;
        }
        catch (IllegalArgumentException e){
            System.err.println("setSpeed Error: " + e.getMessage());
        }
    }

    public int[] getHealthProf() {
        return healthProf;
    }

    public void setHealthProf(int[] healthProf) {
        try{
            if (healthProf == null){
                throw new IllegalArgumentException("Health Proficiencies cannot be null!");
            }
            else if (healthProf.length != healthCategory){
                throw new IllegalArgumentException("Correct number of Health Proficiencies must be maintained!");
            }
            this.healthProf = healthProf;
        }
        catch (IllegalArgumentException e){
            System.err.println("setHealthProf Error: " + e.getMessage());
        }
    }

    public int[] getAttackProf() {
        return attackProf;
    }

    public void setAttackProf(int[] attackProf) {
        try{
            if (attackProf == null){
                throw new IllegalArgumentException("Attack Proficiencies cannot be null!");
            }
            else if (attackProf.length != attackCategory){
                throw new IllegalArgumentException("Correct number of Attack Proficiencies must be maintained!");
            }
            this.attackProf = attackProf;
        }
        catch (IllegalArgumentException e){
            System.err.println("setAttackProf Error: " + e.getMessage());
        }
    }

    public int[] getDefenseProf() {
        return defenseProf;
    }

    public void setDefenseProf(int[] defenseProf) {
        try{
            if (defenseProf == null){
                throw new IllegalArgumentException("Defense Proficiencies cannot be null!");
            }
            else if (defenseProf.length != defenseCategory){
                throw new IllegalArgumentException("Correct number of Defense Proficiencies must be maintained!");
            }
            this.defenseProf = defenseProf;
        }
        catch (IllegalArgumentException e){
            System.err.println("setDefenseProf Error: " + e.getMessage());
        }
    }

    public int[] getStaminaProf() {
        return staminaProf;
    }

    public void setStaminaProf(int[] staminaProf) {
        try{
            if (staminaProf == null){
                throw new IllegalArgumentException("Stamina Proficiencies cannot be null!");
            }
            else if (staminaProf.length != staminaCategory){
                throw new IllegalArgumentException("Correct number of Stamina Proficiencies must be maintained!");
            }
            this.staminaProf = staminaProf;
        }
        catch (IllegalArgumentException e){
            System.err.println("setStaminaProf Error: " + e.getMessage());
        }
    }

    public int[] getSpeedProf() {
        return speedProf;
    }

    public void setSpeedProf(int[] speedProf) {
        try{
            if (speedProf == null){
                throw new IllegalArgumentException("Speed Proficiencies cannot be null!");
            }
            else if (speedProf.length != speedCategory){
                throw new IllegalArgumentException("Correct number of Speed Proficiencies must be maintained!");
            }
            this.speedProf = speedProf;
        }
        catch (IllegalArgumentException e){
            System.err.println("setSpeedProf Error: " + e.getMessage());
        }
    }


    //Setters for individual values in arrays for each
    public void setHealthProfIndiv(int index, int val){
        try{
            if (val < 0){
                throw new IllegalArgumentException("Health Proficiencies cannot be negative!");
            }
            else if (index < 0 || index >= healthCategory){
                throw new IllegalArgumentException("Invalid Health Proficiency accessed!");
            }
            this.healthProf[index] = val;
        }
        catch (IllegalArgumentException e){
            System.err.println("setHealthProfIndiv Error: " + e.getMessage());
        }
    }

    public void setAttackProfIndiv(int index, int val){
        try{
            if (val < 0){
                throw new IllegalArgumentException("Attack Proficiencies cannot be negative!");
            }
            else if (index < 0 || index >= attackCategory){
                throw new IllegalArgumentException("Invalid Attack Proficiency accessed!");
            }
            this.attackProf[index] = val;
        }
        catch (IllegalArgumentException e){
            System.err.println("setAttackProfIndiv Error: " + e.getMessage());
        }
    }

    public void setDefenseProfIndiv(int index, int val){
        try{
            if (val < 0){
                throw new IllegalArgumentException("Defense Proficiencies cannot be negative!");
            }
            else if (index < 0 || index >= defenseCategory){
                throw new IllegalArgumentException("Invalid Defense Proficiency accessed!");
            }
            this.defenseProf[index] = val;
        }
        catch (IllegalArgumentException e){
            System.err.println("setDefenseProfIndiv Error: " + e.getMessage());
        }
    }

    public void setStaminaProfIndiv(int index, int val){
        try{
            if (val < 0){
                throw new IllegalArgumentException("Stamina Proficiencies cannot be negative!");
            }
            else if (index < 0 || index >= staminaCategory){
                throw new IllegalArgumentException("Invalid Stamina Proficiency accessed!");
            }
            this.staminaProf[index] = val;
        }
        catch (IllegalArgumentException e){
            System.err.println("setStaminaProfIndiv Error: " + e.getMessage());
        }
    }

    public void setSpeedProfIndiv(int index, int val){
        try{
            if (val < 0){
                throw new IllegalArgumentException("Speed Proficiencies cannot be negative!");
            }
            else if (index < 0 || index >= speedCategory){
                throw new IllegalArgumentException("Invalid Speed Proficiency accessed!");
            }
            this.speedProf[index] = val;
        }
        catch (IllegalArgumentException e){
            System.err.println("setSpeedProfIndiv Error: " + e.getMessage());
        }
    }

}
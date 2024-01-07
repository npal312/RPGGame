public class Character{
    //superclass because I want some functionality to be inherited

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

    public Character(int healthMax, int attack, int defense, int staminaMax, int speed, int[] healthProf, int[] attackProf, int[] defenseProf, int[] staminaProf, int[] speedProf){
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

    public Character(){
        this.health = 1;
        this.healthMax = 1;
        this.attack = 1;
        this.defense = 1;
        this.stamina = 1;
        this.staminaMax = 1;
        this.speed = 1;
        this.healthProf = new int[1]; //health boost
        this.attackProf = new int[7]; //blunt, sharp, mystic, fire, electric, ice, poison
        this.defenseProf = new int[7]; //blunt, sharp, mystic, fire, electric, ice, poison
        this.staminaProf = new int[2]; //stamina bar, stamina recovery
        this.speedProf = new int[2]; //attack speed, priority speed
    }

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

}
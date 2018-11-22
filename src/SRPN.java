import java.io.*;

/**
 * Program to replicate the features of the original SRPN program, which is a saturation reverse polish notation
 * calculator, meaning it will prevent values wrapping around if it is too big or too small to be stored as an integer.
 * The program also includes some extra features such as using octal numbers, having random numbers using the command "r".
 */

public class SRPN {
    //Fields
    private int maxSize = 23;
    private String stack[] = new String[maxSize];
    private int randomNumbers[] = new int[22];
    private int randomNumberTracker = -1;
    private int stackPointer = -1; //"points" to the top of the stack, -1 at start since nothing is pushed on yet
    private long sum;
    private int operandOne;
    private int operandTwo;

    //Constructor - setting the first value of the stack, and filling the array with 'random' numbers.
    public SRPN() {
        this.stack[0] = "-2147483648";
        this.setRandomNumbers();
    }

    //Methods

    /*
    This method receives the input that the user gives, and processes the contents. Every If and else if will be used
    to check what the operator is to perform on the operands. This includes octal numbers starting with "0", =, +, -,
    *, /, %, ^, d and r.
     */
    public void processCommand(String input) {
        if(input.startsWith("0")){
            //If the input string starts with 0, and doesn't have 8's and 9's, then it is an octal number
            if(!input.contains("8") && !input.contains("9")){
                long octalNumber = Long.parseLong(input, 8); //Convert the octal to a decimal number
                this.push(String.valueOf(octalNumber));
            }
        }
        //If the user types =, it means the top element on the stack needs to be displayed
        else if(input.charAt(input.length()-1) == '=') {
            if(this.stackPointer >= 0){
                System.out.println(this.peek());
            }
            else{
                System.out.println("Stack underflow.");
            }
        }
        //If the user types +, check if you can remove two items from the stack and proceed to carry out the operation
        else if(input.charAt(input.length()-1) == '+') { //Addition
            if(canPopTwice()){
                this.operandTwo = Integer.parseInt(this.pop());
                this.operandOne = Integer.parseInt(this.pop());
                this.sum = (long)this.operandOne + (long)this.operandTwo;
                //Check if the sum is greater than the maximum value an integer can store, in order to prevent
                //wrapping around
                if(this.sum > Integer.MAX_VALUE){
                    this.push(String.valueOf(Integer.MAX_VALUE));
                }
                else if(this.sum < Integer.MIN_VALUE){
                    this.push(String.valueOf(Integer.MIN_VALUE));
                }
                else{
                    this.push(Integer.toString(this.operandOne + this.operandTwo));
                }
            }
            //If you cant pop twice, give a relevant error message
            else{
                System.out.println("Stack underflow.");
            }
        }
        else if(input.charAt(input.length()-1) == '-'){ //Subtraction
            if(canPopTwice()){
                this.operandTwo = Integer.parseInt(this.pop());
                this.operandOne = Integer.parseInt(this.pop());
                this.sum = (long)this.operandOne - (long)this.operandTwo;
                if(this.sum > Integer.MAX_VALUE){
                    this.push(String.valueOf(Integer.MAX_VALUE));
                }
                else if(this.sum < Integer.MIN_VALUE){
                    this.push(String.valueOf(Integer.MIN_VALUE));
                }
                else{
                    this.push(Integer.toString(this.operandOne - this.operandTwo));
                }
            }
            else{
                System.out.println("Stack underflow.");
            }
        }
        else if(input.charAt(input.length()-1) == '*'){ //Multiplication
            if(canPopTwice()){
                this.operandTwo = Integer.parseInt(this.pop());
                this.operandOne = Integer.parseInt(this.pop());
                this.sum = (long)this.operandOne * (long)this.operandTwo;
                if(this.sum > Integer.MAX_VALUE){
                    this.push(String.valueOf(Integer.MAX_VALUE));
                }
                else if(this.sum < Integer.MIN_VALUE){
                    this.push(String.valueOf(Integer.MIN_VALUE));
                }
                else{
                    this.push(Integer.toString(this.operandOne * this.operandTwo));
                }
            }
            else{
                System.out.println("Stack underflow.");
            }
        }
        else if(input.charAt(input.length()-1) == '/'){ //Division
            if(canPopTwice()){
                this.operandTwo = Integer.parseInt(this.pop());
                this.operandOne = Integer.parseInt(this.pop());
                if(this.operandTwo != 0){ //If the denominator is not 0, perform the division
                    this.push(Integer.toString(this.operandOne / this.operandTwo));
                }
                else{ //Division by 0 error message given, and both operands pushed back on stack
                    System.out.println("Divide by 0.");
                    this.push(Integer.toString(this.operandOne));
                    this.push(Integer.toString(this.operandTwo));
                }
            }
            else{
                System.out.println("Stack underflow.");
            }
        }
        else if(input.charAt(input.length()-1) == '%'){ //Modulo division
            if(canPopTwice()){
                this.operandTwo = Integer.parseInt(this.pop());
                this.operandOne = Integer.parseInt(this.pop());
                this.push(Integer.toString(this.operandOne % this.operandTwo));
            }
            else{
                System.out.println("Stack underflow.");
            }
        }
        else if(input.charAt(input.length()-1) == '^'){ //Exponent
            if(canPopTwice()){
                this.operandTwo = Integer.parseInt(this.pop());
                if(this.operandTwo < 0){ //If a negative power is given, throw an error message and push the power back
                    System.out.println("Negative power.");
                    this.push(Integer.toString(operandTwo));
                }
                else{
                    this.operandOne = Integer.parseInt(this.pop());
                    this.sum = (long)this.operandOne ^ (long)this.operandTwo;
                    if(this.sum > Integer.MAX_VALUE){
                        this.push(String.valueOf(Integer.MAX_VALUE));
                    }
                    else if(this.sum < Integer.MIN_VALUE){
                        this.push(String.valueOf(Integer.MIN_VALUE));
                    }
                    else{
                        this.push(Integer.toString((int) Math.pow(this.operandOne, this.operandTwo)));
                    }
                }
            }
            else{
                System.out.println("Stack underflow.");
            }
        }
        //Display contents of the stack command
        else if(input.contains("d")){
            for(char letter: input.toCharArray()){ //Iterate through each letter in the given command
                if(letter == 'd'){
                    this.displayStack();
                }
                else{ //If the command includes letters that aren't 'd' then display error messages for them
                    System.out.println("Unrecognised operator or operand \"" + letter + "\".");
                }
            }
        }
        //Push a random number onto the stack command
        else if(input.contains("r")){
            for(char letter: input.toCharArray()){
                if(letter == 'r'){ //If an r is detected, increase r counter by 1, and return a number for the array
                    this.randomNumberTracker++;
                    if(this.randomNumberTracker > 21){ //If the counter is above 21 (out of bounds), put the counter back to 0
                        this.randomNumberTracker = 0;
                    }
                    this.push(Integer.toString(this.getRandomNumber()));
                }
                else{
                    System.out.println("Unrecognised operator or operand \"" + letter + "\".");
                }
            }
        }
        //In this case the inputted command is a regular number and should be pushed onto the stack
        else{
            //If the values inputted are bigger or smaller than the max/min values, set to the max/min values respectively
            if(Long.parseLong(input) > Integer.MAX_VALUE){
                this.push(String.valueOf(Integer.MAX_VALUE));
            }
            else if(Long.parseLong(input) < Integer.MIN_VALUE){
                this.push(String.valueOf(Integer.MIN_VALUE));
            }
            else{
                this.push(input);
            }
        }
    }

    //Method which will iterate through each element of the stack and print it
    public void displayStack(){
        for(String value: this.stack){
            if(value == null){
                //Don't print the null values
            }
            else{
                System.out.println(value);
            }
        }
    }

    //Method to set the values of the random array when the object is created
    public void setRandomNumbers(){
        this.randomNumbers[0] = 1804289383;
        this.randomNumbers[1] = 846930886;
        this.randomNumbers[2] = 1681692777;
        this.randomNumbers[3] = 1714636915;
        this.randomNumbers[4] = 1957747793;
        this.randomNumbers[5] = 424238335;
        this.randomNumbers[6] = 719885386;
        this.randomNumbers[7] = 1649760492;
        this.randomNumbers[8] = 596516649;
        this.randomNumbers[9] = 1189641421;
        this.randomNumbers[10] = 1025202362;
        this.randomNumbers[11] = 1350490027;
        this.randomNumbers[12] = 783368690;
        this.randomNumbers[13] = 1102520059;
        this.randomNumbers[14] = 2044897763;
        this.randomNumbers[15] = 1967513926;
        this.randomNumbers[16] = 1365180540;
        this.randomNumbers[17] = 1540383426;
        this.randomNumbers[18] = 304089172;
        this.randomNumbers[19] = 1303455736;
        this.randomNumbers[20] = 35005211;
        this.randomNumbers[21] = 521595368;
    }

    public int getRandomNumber(){
        return(this.randomNumbers[randomNumberTracker]);
    }

    public boolean isFull(){
        return(this.stackPointer >= this.maxSize - 1);
    }

    //Method to place an item onto the stack
    public void push(String operand){
        if(isFull()){
            System.out.println("Stack overflow.");
        }
        else{
            this.stackPointer++;
            this.stack[this.stackPointer] = operand;
        }
    }

    //Method to show the top element of the stack without removing it
    public String peek(){
        return(this.stack[this.stackPointer]);
    }

    public boolean canPopTwice(){
        return(this.stackPointer >= 1);
    }

    //Method to remove the top element from the stack
    public String pop(){
        String temp = this.stack[stackPointer]; //Store the top element in a temporary value
        this.stack[stackPointer] = null; //Nullify the element being removed
        this.stackPointer--;
        return temp; //Return the element that was removed
    }

    public static void main(String[] args) throws IOException{
        SRPN srpn = new SRPN(); //Create the new SRPN object
        int hashCount = 0; //Counter to track amount of "#" entered by the user
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        //Keep accepting inputs
        while(true){
            String command = reader.readLine();

            //Close on an End of file
            if(command == null){
                //Exit code 0
                System.exit(0);
            }
            //Iterate through each argument seperated by spaces " " from the users input
            for(String commandComponent : command.split(" ")){
                try{
                    if(commandComponent.contains("#")){ //A comment has started
                        hashCount++;
                    }
                    //If there is an even number of hashes, then commenting has finished and subsequent arguments
                    //can be processed
                    if(hashCount % 2 == 0){
                        if(commandComponent.isEmpty() || commandComponent.equals("#")){
                            //Do nothing as the user has pressed the enter key
                        }
                        else{
                            srpn.processCommand(commandComponent); //Send the input to be processed
                        }
                    }
                } catch(Exception e){
                    //When there is an error from the input from the user, an error message is given to each
                    //individual character
                    for(char letters : commandComponent.toCharArray()){
                        System.out.println("Unrecognised operator or operand \"" + letters + "\".");
                    }
                }
            }
        }
    }
}

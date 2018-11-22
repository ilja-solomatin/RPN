import java.io.*;

public class SRPN {

    private int maxSize = 23;
    private String stack[] = new String[maxSize];
    private int randomNumbers[] = new int[23];
    private int randomNumberTracker = 0;
    private int pointer = -1;
    private long sum;
    private int operandOne;
    private int operandTwo;

    public SRPN() {
        this.stack[0] = "-2147483648";
        this.setRandomNumbers();
    }

    public void processCommand(String input) {
        if(input.startsWith("0")){
            if(!input.contains("8") && !input.contains("9")){
                long octalNumber = Long.parseLong(input, 8);
                this.push(String.valueOf(octalNumber));
            }
            else{
                // Do nothing as 8 and 9 are not octal numbers
            }
        }//
        else if(input.charAt(input.length()-1) == '=') {
            System.out.println(this.peek());
        }
        else if(input.charAt(input.length()-1) == '+') {
            if(canPopTwice()){
                this.operandTwo = Integer.parseInt(this.pop());
                this.operandOne = Integer.parseInt(this.pop());
                this.sum = (long)this.operandOne + (long)this.operandTwo;
                if(this.sum > Integer.MAX_VALUE){
                    this.push(String.valueOf(Integer.MAX_VALUE));
                }
                else if(this.sum < Integer.MIN_VALUE){
                    this.push(String.valueOf(Integer.MIN_VALUE));
                }
                else{
                    this.push(Integer.toString(operandOne + operandTwo));
                }
            }
            else{
                System.out.println("Stack underflow.");
            }
        }
        else if(input.charAt(input.length()-1) == '-'){
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
                    this.push(Integer.toString(operandOne - operandTwo));
                }
            }
            else{
                System.out.println("Stack underflow.");
            }
        }
        else if(input.charAt(input.length()-1) == '*'){
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
                    this.push(Integer.toString(operandOne * operandTwo));
                }
            }
            else{
                System.out.println("Stack underflow.");
            }
        }
        else if(input.charAt(input.length()-1) == '/'){
            if(canPopTwice()){
                this.operandTwo = Integer.parseInt(this.pop());
                this.operandOne = Integer.parseInt(this.pop());
                if(this.operandTwo != 0){
                    this.push(Integer.toString(operandOne / operandTwo));
                }
                else{
                    System.out.println("Divide by 0.");
                    this.push(Integer.toString(this.operandOne));
                    this.push(Integer.toString(this.operandTwo));
                }
            }
            else{
                System.out.println("Stack underflow.");
            }
        }
        else if(input.charAt(input.length()-1) == '%'){
            if(canPopTwice()){
                this.operandTwo = Integer.parseInt(this.pop());
                this.operandOne = Integer.parseInt(this.pop());
                this.push(Integer.toString(operandOne % operandTwo));
            }
            else{
                System.out.println("Stack underflow.");
            }
        }
        else if(input.charAt(input.length()-1) == '^'){
            if(canPopTwice()){
                this.operandTwo = Integer.parseInt(this.pop());
                if(this.operandTwo < 0){
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
                        this.push(Integer.toString((int) Math.pow(operandOne, operandTwo)));
                    }
                }
            }
            else{
                System.out.println("Stack underflow.");
            }
        }
        else if(input.contains("d")){
            for(char letter: input.toCharArray()){
                if(letter == 'd'){
                    this.displayStack();
                }
                else{
                    System.out.println("Unrecognised operator or operand \"" + letter + "\".");
                }
            }
        }
        else if(input.contains("r")){
            for(char letter: input.toCharArray()){
                if(letter == 'r'){
                    this.push(Integer.toString(this.getRandomNumber()));
                    if(this.randomNumberTracker > 22){
                        this.randomNumberTracker = 0;
                    }
                    else{
                        this.randomNumberTracker++;
                    }
                }
                else{
                    System.out.println("Unrecognised operator or operand \"" + letter + "\".");
                }
            }
        }
        else{
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

    public void displayStack(){
        for(String value: this.stack){
            if(value == null){
                //Don't print the null value
            }
            else{
                System.out.println(value);
            }
        }
    }

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
        this.randomNumbers[22] = 1804289383;
    }

    public int getRandomNumber(){
        return(this.randomNumbers[randomNumberTracker]);
    }

    public boolean isFull(){
        return(this.pointer >= this.maxSize -1);
    }

    public void push(String operand){
        if(isFull()){
            System.out.println("Stack overflow.");
        }
        else{
            this.pointer++;
            this.stack[pointer] = operand;
        }
    }

    public String peek(){
        return(this.stack[pointer]);
    }

    public boolean canPopTwice(){
        return(this.pointer >= 1);
    }

    public String pop(){
        String temp = this.stack[pointer];
        this.stack[pointer] = null;
        this.pointer--;
        return temp;
    }

    public static void main(String[] args) throws IOException {
        SRPN srpn = new SRPN();
        int hashCount = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true){
            String command = reader.readLine();

            //Close on an End-of-file (EOF) (Ctrl-D on the terminal)
            if(command == null) {
                //Exit code 0 for a graceful exit
                System.exit(0);
            }
            for(String commandComponent: command.split(" ")){
                try{
                    if(commandComponent.contains("#")){
                        hashCount++;
                    }
                    if(hashCount % 2 == 0){
                        if(commandComponent.isEmpty() || commandComponent.equals("#")){
                            //Do nothing as the user has pressed the enter key
                        }
                        else{
                            srpn.processCommand(commandComponent);
                        }
                    }
                } catch(Exception e){
                    for(char letters: commandComponent.toCharArray()){
                        System.out.println("Unrecognised operator or operand \"" + letters + "\".");
                    }
                }
            }
        }
    }
}

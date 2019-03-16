package mini2;


import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * 
 * @author Keven Lin
 *
 */
public class CS227Comp extends java.lang.Object {
	/**
	 * Opcode for the read instruction.
	 */
	public static final int READ = 10;
	/**
	 * Opcode for the write instruction.
	 */
	public static final int WRITE = 20;
	/**
	 * Opcode for the load instruction.
	 */
	public static final int LOAD = 30;
	/**
	 * Opcode for the store instruction.
	 */
	public static final int STORE = 40;
	/**
	 * Opcode for the add instruction.
	 */
	public static final int ADD = 50;

	/**
	 * Opcode for the sub instruction.
	 */
	public static final int SUB = 51;

	/**
	 * Opcode for the div instruction.
	 */
	public static final int DIV = 52;

	/**
	 * Opcode for the mod instruction.
	 */
	public static final int MOD = 53;

	/**
	 * Opcode for the mul instruction.
	 */
	public static final int MUL = 54;
	/**
	 * Opcode for the jump instruction.
	 */
	public static final int JUMP = 60;

	/**
	 * Opcode for the jumpn (jump if negative) instruction.
	 */
	public static final int JUMPN = 61;

	/**
	 * Opcode for the jumpz (jump if zero) instruction.
	 */
	public static final int JUMPZ = 63;
	/**
	 * Opcode for the halt instruction.
	 */
	public static final int HALT = 80;
	/**
	 * Constructs a machine with the given number of words of memory, all words
	 * zero, all registers zero, in a halted state.
	 */
	
	private boolean halt = false;
	private int length;
	private int[] memory;
	private int opcode;
	private int operand;
	private int accum= 0;
	private int counter = 0;
	private int reg= 0;
	
	
	
	public CS227Comp(int memorySize) {
		length = memorySize;
		memory = new int[memorySize];
		halt = true;
	}

	/**
	 * Constructs a machine with the given initial values in the instruction counter
	 * and accumulator, and the given memory contents. The memory size will be that
	 * of the given array. Immediately crashes the machine if the given memory
	 * contains any invalid instructions, as specified in loadMemoryImage.
	 * 
	 * @param initialIC
	 * @param initialAC
	 * @param memoryImage
	 */
	public CS227Comp(int initialIC, int initialAC, int[] memoryImage) {
		counter = initialIC;
		accum= initialAC;
		length = memoryImage.length;
		memory = memoryImage;
	}

	/**
	 * Returns the current value in the accumulator.
	 * 
	 * @return accumulator current value in the accumulator
	 */
	public int getAC() {
		return accum;
	}

	/**
	 * Returns the current value of the instruction counter.
	 * 
	 * @return current value of the instruction counter
	 */
	public int getIC() {
		return counter;
	}

	/**
	 * Returns the contents of the instruction register (most recently executed
	 * instruction)
	 * 
	 * @return contents of the instruction register
	 */
	public int getIR() {
		return reg;
	}

	/**
	 * Returns the opcode for the most recently executed instruction (instruction
	 * register divided by 100).
	 * 
	 * @return opcode for the most recently executed instruction
	 */
	public int getOpcode() {

		return opcode;
	}

	/**
	 * Returns the operand for the most recently executed instruction (instruction
	 * register modulo 100).
	 * 
	 * @return operand for the most recently executed instruction
	 */
	public int getOperand() {
		return operand;
	}

	/**
	 * Returns true if the machine is in a halted state, false otherwise.
	 * 
	 * @return true if the machine is in a halted state, false otherwise
	 */
	public boolean isHalted() {
		if (halt == true) {
			return true;
		} else
			return false;
	}

	/**
	 * Returns the contents of the memory cell at the given address.
	 * 
	 * @param address memory address
	 * @return contents of memory cell at the given address
	 */
	public int peekMemory(int address) {

		return memory[address];
	}

	/**
	 * Returns the number of words of memory this machine has.
	 * 
	 * @return the number of words of memory
	 */
	public int getMemorySize() {
		return length;
	}

	/**
	 * Executes one instruction at a time, over and over, as long as the machine is
	 * not halted.
	 */
	public void runProgram() {
		while (!halt) {
			nextInstruction();
		}
	}

	/**
	 * Displays complete machine state. This one is done for you. Observe the
	 * conversions that are used to print the values, as you'll need them elsewhere
	 * if you want a uniform look to your output.
	 */
	public void dumpCore() {
		System.out.printf("REGISTERS:\n");
		System.out.printf("%-20s %+05d\n", "accumulator", getAC());
		System.out.printf("%-20s    %02d\n", "instruction counter", getIC());
		System.out.printf("%-20s %+05d\n", "instruction register", getIR());
		System.out.printf("%-20s    %02d\n", "operation code", getIR() / 100);
		System.out.printf("%-20s    %02d\n", "operand", getIR() % 100);
		System.out.printf("\nMEMORY:\n  ");
		for (int i = 0; i < 10; i++) {
			System.out.printf("%6d", i);
		}
		int row = 0;
		for (int i = 0; i < getMemorySize(); i++) {
			if (i % 10 == 0) {
				row ++;
				System.out.printf("\n%2d ", row * 10);
			}
			System.out.printf("%+05d ", peekMemory(i));
		}
		System.out.println();
	}

	/**
	 * Loads the given values into the machine's memory. If the length of the given
	 * array is smaller than this machine's memory size, the remaining cells are
	 * filled with zeros; if larger, extra values are ignored. If any value is
	 * encountered in the given array that is not a valid instruction, the machine
	 * crashes at that point with message "*** Invalid input ***". The machine's
	 * memory size is not modified. If no invalid instructions are encountered, the
	 * machine will be in a non-halted state.
	 * 
	 * @param image memory image to load
	 */
	public void loadMemoryImage(int[] image) {
		
		for (int i = 0; i < memory.length; i++) {
			try {
				if (image[i] < -9999 || image[i] > 9999) {
					halt = true;
					System.out.println("*** Invalid input***");
					return;
				}
				memory[i] = image[i];
			} 
			
			catch (ArrayIndexOutOfBoundsException e) {
				memory[i] = 0;
			}
		}
		halt = false;

	}

	/**
	 * Reads instructions from the terminal, one per line, until the sentinel value
	 * (-99999) is read. Instructions are decimal integers in the range
	 * [-9999,9999]. Any invalid input should immediately crash the machine with
	 * error message "*** Invalid input ***". Each instruction should be prompted
	 * for with the zero-padded, two digit sequential instruction number followed by
	 * a question mark. The instruction or sentinel should then be echoed as a four
	 * digit (or five, for the sentinel), signed, zero-padded decimal integer. After
	 * successfully loading, display the message "*** Program Loaded ***" and move
	 * to a running state.
	 */
	public void loadProgramFromConsole() {
		
		Scanner scan = new Scanner(System.in);

		for (int i = 0; i < memory.length; i++) {
			int read = scan.nextInt();

			if (read < -9999 || read > 9999) {
				if (read < -99999) 
					i = memory.length;
				
				else {
					halt = true;
					dumpCore();
					return;
				}
			}
			memory[i] = read;
		}
	}

	/**
	 * Reads instructions from the given file. Instructions are then loaded into
	 * memory according to the specification for loadMemoryImage.
	 * 
	 * @param filename file from which to read instructions
	 */
	public void loadProgramFromFile(String filename) throws FileNotFoundException {
		File file = new File(filename);
		Scanner scan = new Scanner(file);

		for (int i = 0; i < memory.length; i++) {
			
			if (scan.hasNextLine()) {
				
				int read = scan.nextInt();

				if (read < -9999 || read > 9999) {
					
					if (read < -99999) 
						i = memory.length;
					
					else 
					{
						halt = true;
						dumpCore();
						System.out.println("*** Invalid Input ***");
						scan.close();
						return;
					}
				}
				memory[i] = read;
			} 
			else 
				i = memory.length;
		}
		scan.close();
	}

	/**
	 * Loads the next instruction from memory, parses it for the opcode and operand,
	 * and executes the instruction. The opcode is the high-order two digits of the
	 * instruction; the operand is the low-order two digits. Except in case of a
	 * jump, the instruction counter is incremented by one following execution of
	 * the instruction.
	 * <p>
	 * Invalid opcodes crash the machine.
	 * <p>
	 * Descriptions of all instructions follow:
	 *
	 * <ul>
	 * <li>READ: Executes the read instruction. Reads a decimal word from the
	 * terminal into the address referenced by operand and updates the instruction
	 * counter. Valid words are in the range [-9999,9999]. Out of range words are
	 * truncated on the right until within range before being stored; the truncated
	 * portion is discarded. For example, -723471 will be truncated to -7234.
	 *
	 * <li>WRITE: Displays the value stored in memory at the address referenced by
	 * the operand as a signed, four digit, zero padded decimal integer and updates
	 * the instruction counter.
	 *
	 * <li>LOAD: Loads the value stored in memory at the address referenced by
	 * operand into the accumulator and updates the instruction counter.
	 *
	 * <li>STORE: Stores the value in the accumulator into memory at the address
	 * referenced by the operand and updates the instruction counter.
	 *
	 * <li>ADD: Adds the value stored in memory at the address referenced by operand
	 * to the accumulator, accounting for overflow, and updates the instruction
	 * counter.
	 *
	 * <li>SUB: Subtracts the value stored in memory at the address referenced by
	 * operand from the accumulator, accounting for overflow, and updates the
	 * instruction counter.
	 *
	 * <li>DIV: Divides the accumulator by the value stored in memory at the address
	 * referenced by operand, accounting for overflow, and updates the instruction
	 * counter. All division is integer division. Division by zero crashes the
	 * machine.
	 *
	 * <li>MOD: Calculates the remainder when dividing the accumulator by the value
	 * stored in memory at the address referenced by operand, accounting for
	 * overflow, stores the result in the accumulator, and updates the instruction
	 * counter. All division is integer division. Division by zero crashes the
	 * machine.
	 *
	 * <li>MUL: Multiplies the accumulator by the value stored in memory at the
	 * address referenced by operand, accounting for overflow, and updates the
	 * instruction counter.
	 *
	 * <li>JUMP: Changes the instruction counter to operand.
	 *
	 * <li>JUMPN: If the accumulator is negative, changes the instruction counter to
	 * operand, otherwise updates the instruction counter normally.
	 *
	 * <li>JUMPZ: If the accumulator is zero, changes the instruction counter to
	 * operand, otherwise updates the instruction counter normally.
	 *
	 * <li>HALT: Displays the message "*** Program terminated normally ***", halts
	 * the machine, and dumps core.
	 * </ul>
	 * Arithmetic overflow occurs when the accumulator acquires a value outside of
	 * the range [-9999,9999]. It is handled by truncating the value of the
	 * accumulator to the low order four digits.
	 * <p>
	 * Instruction counter overflow occurs when when the value of the instruction
	 * counter matches or exceeds the memory size. It is handled by setting the
	 * instruction counter to zero.
	 * <p>
	 * All crashes dump core.
	 */
	public void nextInstruction() {
		reg = memory[counter];
		counter++;
		opcode = reg / 100;
		operand = reg - (opcode * 100);

		if (opcode == LOAD)
			accum = memory[operand];

		else if (opcode == ADD)
			accum += memory[operand];
		
		else if (opcode == SUB)
			accum -= memory[operand];

		else if (opcode == DIV)
		{
	
			if (memory[operand] == 0) {
				halt = true;
				dumpCore();
			} 
			else 
				accum /= memory[operand];
			
		}
		
		else if (opcode == MOD)
			accum %= memory[operand];

		else if (opcode == MUL)
			accum *= memory[operand];

		else if (opcode == STORE)
			memory[operand] = accum;

		else if (opcode == JUMP)
			counter = operand;


		else if (opcode == JUMPN)
		{
			if (accum < 0) 
				counter = operand;
		}
		
		else if (opcode == JUMPZ)
		{
			if (accum == 0) 
				counter = operand;
		}
		
		else if (opcode == WRITE)
		{
			if (memory[operand] < 0)
				System.out.print("-");
			
			else 
				System.out.print("+");
		
			System.out.printf("%04d", memory[operand]);
			System.out.println("");
		}
		
		else if (opcode == HALT)
		{
			halt = true;
			System.out.println("Program terminated normally");
			dumpCore();
		}
		
		else if (opcode == READ)
		{
			Scanner scan = new Scanner(System.in);
			int READ = scan.nextInt();

			while ( READ < -9999 || READ > 9999) 
				READ /= 10;
			
			memory[operand] = READ;
		}
		
		else
		{
			halt = true;
			dumpCore();
		}
		
		if (counter >= memory.length) {
			counter = 0;
		}
		
		if (accum < -9999) {
			accum %= 10000;
		}
		
		if (accum > 9999) {
			accum %= 10000;
		}
		
		
	}
}

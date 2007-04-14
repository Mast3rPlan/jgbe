public class TestSuite
{
	private CPU cpu;
	private Cartridge cartridge;

	public TestSuite()
	{
	}

	public int diagnose( boolean verbose ) {
		return 0;
	}

	public long runinstr(int b1) {
		return runinstr(b1, 0, 0);
	}

	public long runinstr(int b1, int b2) {
		return runinstr(b1, b2, 0);
	}

	public void nothing() {
	}

	public long runinstr(int b1, int b2, int b3) {
		cartridge.MM_ROM[0][0x100] = b1;
		cartridge.MM_ROM[0][0x101] = b2;
		cartridge.MM_ROM[0][0x102] = b3;
		Disassembler deasm = new Disassembler(cpu);
		String s = deasm.simple_disasm(0x100);
		if (s.indexOf("**MISSING    INSTRUCTION**") > -1)
			return 0;
		System.out.printf("%-40s",s);
		int ticks = 4194304;
		long ct1 = System.currentTimeMillis();
		for (; ticks > 0;) {
			cpu.PC=0x100;
			ticks -= cpu.nextinstruction();
			//cpu.cycles();
		}
		long ct2 = System.currentTimeMillis();
		System.out.printf("%5dms\n",ct2-ct1);
		return ct2-ct1;
	}

	public final void run( String[] args ) {

			if (args.length == 0) {
				System.out.println();
				System.out.println("ERROR: missing argument");
				System.out.println();
				System.out.println("USAGE: java TestSuite ROMFILE");
				System.out.println();
				return;
			}
			cartridge = new Cartridge(args[0]);
			if(cartridge.getError()!=null) {
				System.out.println("ERROR: "+cartridge.getError());
				return;
			}

			System.out.println("Succesfully loaded ROM :)");
			cpu = new CPU(cartridge);

			runinstr(0xf3);             // DI
			runinstr((0x06+(4<<3)), 0xff);
			runinstr((0x06+(5<<3)), 0xa0);
			System.out.println("Starting Tests...");
			long time;
			for (int i = 0; i < 0x100; ++i) {
				time = runinstr(0xcb, i, 0x80);            //NOP
			}
			//time = runinstr(0xf3);            //NOP
			//System.out.printf("DI        : %6d\n", time);
			//time = runinstr(0x00);            //NOP
			//System.out.printf("NOP       : %6d\n", time);
			//time = runinstr(0xc3, 0x34, 0x45);            //CPL
			//System.out.printf("JUMP IMM16: %6d\n", time);
			//time = runinstr(4194304/4, 0xfa, 0x80, 0xff);//CPL
			//System.out.printf("1.000.000x LD A,(nn): %6d\n", time);

	}

	public static void main( String[] args ) {
		final TestSuite suite = new TestSuite();
		suite.run(args);
	}
}
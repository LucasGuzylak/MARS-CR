package mars.mips.instructions.customlangs;
import mars.simulator.*;
import mars.mips.hardware.*;
import mars.mips.instructions.*;
import mars.*;
import mars.util.*;
import java.util.Random;

public class ClashRoyale extends CustomAssembly {
    //game state register names
    private static final int troop0 = 8;
    private static final int troop1 = 9;
    private static final int troop2 = 10;
    private static final int troop3 = 11;
    private static final int player_total = 12;
    private static final int enemy_troop0 = 13;
    private static final int elixir = 14;
    private static final int enemy_troop1 = 15;
    private static final int enemy_total = 25;
    private static final int king_hp = 16;
    private static final int crown_hp = 17;
    private static final int cmp_flag = 18;
    private static final int player_crowns = 19;
    private static final int enemy_crowns = 20;
    private static final int timer_reg = 21;

    @Override
    public String getName(){
        return "Clash Royale";
    }

    @Override
    public String getDescription(){
        return "Clash Royale simulation in MIPS 32, this is why we clash!";
    } 

    @Override
    protected void populate(){
        instructionList.add(
                new BasicInstruction("plc $t0",
            	 "Place player troop",
                BasicInstructionFormat.R_FORMAT,
                "000000 00000 00000 fffff 00000 100000",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int reg = statement.getOperands()[0];
                     int v = RegisterFile.getValue(reg);
                     RegisterFile.updateRegister(reg, v+1);
                     int total = RegisterFile.getValue(player_total);
                     RegisterFile.updateRegister(player_total, total+1);
                     SystemIO.printString("total = " + (total+1) + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("rip $t0",
            	 "Player troop dies",
                BasicInstructionFormat.R_FORMAT,
                "000000 00000 00000 fffff 00000 100001",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int reg = statement.getOperands()[0];
                     int v = Math.max(RegisterFile.getValue(reg)-1, 0);
                     RegisterFile.updateRegister(reg, v);
                     int total = Math.max(RegisterFile.getValue(player_total)-1, 0);
                     RegisterFile.updateRegister(player_total, total);
                     SystemIO.printString("total = " + total + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("erip $t5",
            	 "Enemy troop dies",
                BasicInstructionFormat.R_FORMAT,
                "000000 00000 00000 fffff 00000 100010",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                        int reg = statement.getOperands()[0];
                        int v = Math.max(RegisterFile.getValue(reg)-1, 0);
                        RegisterFile.updateRegister(reg, v);
                        int total = Math.max(RegisterFile.getValue(enemy_total)-1, 0);
                        RegisterFile.updateRegister(enemy_total, total);
                        SystemIO.printString("total = " + total + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("evo $t0, $t1",
            	 "Upgrade troop",
                BasicInstructionFormat.R_FORMAT,
                "000000 sssss 000000 fffff 00000 100011",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int[] ops = statement.getOperands();
                     int rd = ops[0];
                     int rs = ops[1];
                     int val = RegisterFile.getValue(rs)+1;
                     RegisterFile.updateRegister(rd, val);
                     SystemIO.printString("evo: $" + rd + " upgraded from level " + RegisterFile.getValue(rs) + " to level " + val + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("elix $t6, 4",
            	 "Collect elixir",
                BasicInstructionFormat.I_FORMAT,
                "000001 00000 fffff ssssssssssssssss",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int rt = statement.getOperands()[0];
                     int imm = statement.getOperands()[1] << 16 >> 16;
                     int v = RegisterFile.getValue(rt) + imm;
                     RegisterFile.updateRegister(rt, v);
                     SystemIO.printString("elix: $" + rt + " collected " + imm +
                                           " elixir → now " + v + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("spend $t6, 3",
            	 "Spend elixir",
                BasicInstructionFormat.I_FORMAT,
                "000010 00000 fffff ssssssssssssssss",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int rt = statement.getOperands()[0];
                     int imm = statement.getOperands()[1] << 16 >> 16;
                     int v = RegisterFile.getValue(rt);
                     int newv = Math.max(v - imm, 0);
                     RegisterFile.updateRegister(rt, newv);
                     SystemIO.printString("spend: $" + rt + " spent " + imm + " you have " + newv + " elixir now \n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("att $t0",
            	 "Attack enemy",
                BasicInstructionFormat.R_FORMAT,
                "000000 0000000000 fffff 00000 100110",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int rs = statement.getOperands()[0];
                     int atk = RegisterFile.getValue(rs);
                     int enemy = RegisterFile.getValue(enemy_total);
                     int newEnemy = Math.max(enemy - atk, 0);
                     RegisterFile.updateRegister(enemy_total, newEnemy);
                     SystemIO.printString("att: total enemies: " + enemy + " - " + atk + " new total enemies: " + newEnemy + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("def $s1, $t1",
            	 "Tower defense",
                BasicInstructionFormat.R_FORMAT,
                "000000 sssss 00000 fffff 00000 100111",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int rd = statement.getOperands()[0];
                     int rs = statement.getOperands()[1];
                     int newv = RegisterFile.getValue(rd) + RegisterFile.getValue(rs);
                     RegisterFile.updateRegister(rd, newv);
                     SystemIO.printString("def: $" + rd + " increased by $" + rs +  " new value: " + newv + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("timer $s5",
            	 "Timer decrement",
                BasicInstructionFormat.R_FORMAT,
                "000000 0000000000 fffff 00000 101000",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int rd = statement.getOperands()[0];
                     int newv = Math.max(RegisterFile.getValue(rd) - 1, 0);
                     RegisterFile.updateRegister(rd, newv);
                     SystemIO.printString("timer: $" + rd + " decremented -> " + newv + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("match label",
            	 "Conceptual branch",
                BasicInstructionFormat.J_FORMAT,
                "000000 ffffffffffffffffffffffff",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     SystemIO.printString("New match!\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("rage $t1",
            	 "Double value",
                BasicInstructionFormat.R_FORMAT,
                "000000 0000000000 fffff 00000 101010",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int r = statement.getOperands()[0];
                     int v = RegisterFile.getValue(r);
                     RegisterFile.updateRegister(r, v*2);
                     SystemIO.printString("rage: $" + r + " doubled -> " + (v*2) + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("freeze $t6",
            	 "Zero register",
                BasicInstructionFormat.R_FORMAT,
                "000000 0000000000 fffff 00000 101011",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int r = statement.getOperands()[0];
                     RegisterFile.updateRegister(r, 0);
                     SystemIO.printString("freeze: $" + r + " frozen -> value set to 0\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("mirror $t1",
            	 "Add troop",
                BasicInstructionFormat.R_FORMAT,
                "000000 0000000000 fffff 00000 101100",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int total = RegisterFile.getValue(player_total) + 1;
                     RegisterFile.updateRegister(player_total, total);
                     SystemIO.printString("mirror: spawned extra troop -> total = " + total + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("fire $t9, 2",
            	 "Fireball",
                BasicInstructionFormat.I_FORMAT,
                "000011 00000 fffff ssssssssssssssss",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int imm = statement.getOperands()[1] << 16 >> 16;
                     int old = RegisterFile.getValue(enemy_total);
                     int newv = Math.max(old - imm, 0);
                     RegisterFile.updateRegister(enemy_total, newv);
                     SystemIO.printString("fire: Fireball hit! total enemies: " + old +  " -> " + newv + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("ot $t6",
            	 "Overtime elixir",
                BasicInstructionFormat.R_FORMAT,
                "000000 0000000000 fffff 00000 110000",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int r = statement.getOperands()[0];
                     int newv = RegisterFile.getValue(r) + 2;
                     RegisterFile.updateRegister(r, newv);
                     SystemIO.printString("ot: overtime bonus! $" + r + " +2 elixir -> " + newv + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("crown $s3",
            	 "Earn crown",
                BasicInstructionFormat.R_FORMAT,
                "000000 0000000000 fffff 00000 110001",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int r = statement.getOperands()[0];
                     int newv = RegisterFile.getValue(r) + 1;
                     RegisterFile.updateRegister(r, newv);
                     SystemIO.printString("crown: $" + r + " earned a crown -> total = " + newv + "\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("cmp $s2, $s3, $s4",
            	 "Compare crowns",
                BasicInstructionFormat.R_FORMAT,
                "001010 sssss ttttt fffff 00000 000000",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     int rd = statement.getOperands()[0];
                     int rs = statement.getOperands()[1];
                     int rt = statement.getOperands()[2];
                     int a = RegisterFile.getValue(rs);
                     int b = RegisterFile.getValue(rt);
                     RegisterFile.updateRegister(rd, a > b ? 1 : 0);
                     SystemIO.printString("cmp: Player vs. Enemy crowns: " + "(" + a +  ") to $" + rt + "(" + b + ")\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("emote",
            	 "Emoji",
                BasicInstructionFormat.R_FORMAT,
                "000010 00000000000000000000000000",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     SystemIO.printString(";D  (player taunts!)\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("lose",
            	 "Lose game",
                BasicInstructionFormat.J_FORMAT,
                "000011 ffffffffffffffffffffffff",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     SystemIO.printString("Game Over: Player loses!\n");
                  }
               }));
        instructionList.add(
                new BasicInstruction("win",
            	 "Win game",
                BasicInstructionFormat.J_FORMAT,
                "000100 ffffffffffffffffffffffff",
                new SimulationCode()
               {
                   public void simulate(ProgramStatement statement) throws ProcessingException
                  {
                     SystemIO.printString("Victory: Player wins!\n");
                  }
               }));
    }
}
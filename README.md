# MARS-CR (Clash Royale Custom Language)

MARS-CR is a 32-bit, MIPS-inspired Assembly language simulating Clash Royale mechanics. It supports troop placement and removal, tower HP and crown tracking, elixir collection and spending, spells, match timer, and win/lose outcomes based on crowns.

 Troops & Levels:
plc $tX — place a troop
rip $tX — player troop dies
erip $tX — enemy troop dies
evo $rd, $rs — upgrade troop level

Elixir:
elix $rt, imm — collect elixir
spend $rt, imm — spend elixir
ot $rt — overtime +2 elixir
freeze $rt — set register to 0
rage $rt — double register

Combat & Towers:
att $rs — attack enemy troops
def $rd, $rs — reinforce tower
fire $rt, imm — fireball damage
mirror $rt — spawn extra troop

Crowns, Timers & Match Logic:
crown $rt — earn crown
cmp $rd, $rs, $rt — crown comparison flag
timer $rt — decrement timer
emote — prints emoji
win — match victory
lose — match defeat
match label — conceptual branch placeholder

How to use:
1. Go to the MARS-CR repo and click the green CODE button then copy the URL.
2. Open your terminal and type "git clone URL" to clone the repo.
3. Type "cd MARS-CR" to change directory to MARS-CR.
4. Type "java -jar BuildCustomLang.jar ClashRoyale.java" to compile the java file into Assembly.
5. Open Mars.jar in the MARS-CR file.
6. Open a new file by clicking the white square in the top left corner.
7. Click "Tools" -> "Language Switcher" -> "Select Language" -> "Clash Royale"
8. Try out the different instructions! Follow the guide above.

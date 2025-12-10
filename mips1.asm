# --- MATCH 3: Close match, overtime win ---

    elix $t6, 4
    plc  $t0
    att  $t0             # early pressure
    crown $s3            # player gets 1 crown

    crown $s4            # enemy gets 1 crown (1–1)

    timer $s5            # end of regulation
    ot   $t6             # overtime elixir boost
    rage $t1             # troop becomes stronger
    def  $s1, $t1        # tower reinforced
    freeze $t6           # freeze remaining elixir (dramatic!)

    fire $t9, 2
    att  $t0             # finishing blow

    crown $s3            # player takes tower in OT (2–1)

    cmp $s2, $s3, $s4    # player (2) vs enemy (1)

    win

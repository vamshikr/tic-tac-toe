// MIT License

// Copyright (c) 2009 Vamshi Basupalli

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.




import java.io.*;

public class TicTacToe {

    private int [] grid;
    private int size;

    private class Move{
        int row;
        int col;
        int val;

        public Move (int row, int col, int val)
        {
            this.row = row;
            this.col = col;
            this.val = val;
        }

        public void setMove (int row, int col, int val)
        {
            this.row = row;
            this.col = col;
            this.val = val;
        }

        public int getVal ()
        {
            return this.val;
        }

        public int getRow ()
        {
            return this.row;
        }

        public int getCol ()
        {
            return this.col;
        }
    }

    public TicTacToe (int size)
    {
      this.size = size;
      grid = new int[this.size * this.size];

      /* initialize grid */
      for (int i = 0; i < this.size; i++)
      {
          for (int j = 0; j < this.size; j++)
          {
              grid[i*this.size + j] = +9;   /* default +9 */
          }
      }
    }

    public void displayGrid ()
    {

     for (int i = 0; i < this.size; i++)
      {
          for (int j = 0; j < this.size; j++)
          {
              if (9 ==  grid[i*this.size + j])
              {
                  System.out.print(" -- ");
              }else if (1 ==  grid[i*this.size + j])
              {
                  System.out.print(" X ");
              }else
              {
                  System.out.print(" 0 ");
              }
          }

          System.out.print("\n");
      }

    }

    public Boolean markInGrid (int row, int column, int mark)
    {
        return markInGrid (this.grid, this.size, row, column, mark);
    }
    public Boolean markInGrid (int []grid, int size, int row, int column, int player)
    {
        Boolean ret_val = false;
        int mark;

        if (0 == player)       /* cell is marker with +1 for human player :) */
        {
            mark = +1;
        }else                 /* for computer the cell is marked with -1 */
        {
            mark = -1;
        }

        if ((row >= 0) && (row < size)
                && (column >= 0) && (column < size))
        {                        
            if (9 == grid[row*size + column])
            {
                grid[row*size + column] = mark;
                ret_val = true;
            }
        }
        return ret_val;
    }

    /*
     *  0 - if its draw
     * -1 - if Computer wins
     * +1 - if Player 1 wins
     * +9 - if no result yet (less than 9 moves)
     */

    public int evaluateGrid (int num_moves)
    {
        return evaluateGrid (this.grid, this.size, num_moves);
    }

    public int evaluateGrid (int []grid, int size, int num_moves)
    {
        int val;
        int i, j;

        /* row wise */
        for (i = 0; i < size; i++)
        {
            val = grid[i * size];

            if (9 != val)
            {
                for (j = 1; j < size; j++)
                {
                    if (!((9 != grid[i*size + j])
                            && (val == grid[i*size + j])))
                    {
                        break;
                    }
                }

                if (j == size)
                {
                    return val;
                }
            }
        }/* row wise */


        /* column wise */
        for (j = 0; j < size; j++)
        {
            val = grid[j];

            if (9 != val)
            {
                for (i = 1; i < size; i++)
                {
                    if (!((9 != grid[i*size + j])
                            && (val == grid[i*size + j])))
                    {
                        break;
                    }
                }

                if (i == size)
                {
                    return val;
                }
            }
        }/* column wise */

        /* diagonal 1,1, 2,2, 3,3....*/
        val = grid[0];

        for ( i = 1, j = 1; i < size; i++, j++)
        {
            if (!((9 != grid[i*size + j])
                    && (val == grid[i*size + j])))
            {
                break;
            }
        }

        if (i == size)
        {
            return val;
        }

        /* diagonal 1,size, 2,size-1,..... */
        val = grid[size-1];

        for ( i = 1, j = size - 2; i < size; i++, j--)
        {
            if (!((9 != grid[i*size + j])
                    && (val == grid[i*size + j])))
            {
                break;
            }
        }

        if (i == size)
        {
            return val;
        }

        if (num_moves < 9)
        {
            return 9;
        }else
        {
            return 0;
        }
    }

    public int[] machineMove (int min_max, int num_moves)
    {
        int [] rc = new int[2];
        
        Move move = minMove (this.grid, this.size, num_moves);

        rc[0] = move.getRow();
        rc[1] = move.getCol();

        return rc;
    }

    
    private Move maxMove (int grid[], int size, int num_moves)
    {
        int i, j;
        Move best_move = new Move (-1, -1, -10);
        int []new_grid = new int[size*size];
        Move temp_move = new Move (-1, -1, -10);

        
        for (i = 0; i < size; i++)
        {
            for (j = 0; j < size; j++)
            {
                if (9 == grid[i*size + j])
                {
                    System.arraycopy (grid, 0, new_grid, 0, size*size);
                    if (true == markInGrid (new_grid, size, i, j, 0))
                    {
                        /* only if we have finished more than 5 moves,
                            we need check if any player has won */
                        if ((num_moves + 1) >= (2*size - 1))
                        {                            
                            int result = evaluateGrid (new_grid, size, num_moves + 1);

                            /* if no result yet, then go a level
                                more in search tree */
                            if (9 == result)
                            {
                                temp_move = minMove (new_grid, size, num_moves + 1);
                            }else
                            {
                                temp_move.setMove(i, j, result);
                            }

                        }else
                        {
                            temp_move = minMove (new_grid, size, num_moves + 1);
                        }

                        if (temp_move.val > best_move.val)
                        {
                            best_move.val = temp_move.val;
                            best_move.row = i;
                            best_move.col = j;
                        }
                    }
                }
            }
        }

        return best_move;
    }

    private Move minMove (int grid[], int size, int num_moves)
    {
        int i, j;
        Move best_move = new Move (-1, -1, +10);
        int []new_grid = new int[size*size];
        Move temp_move = new Move (-1, -1, +10);


        for (i = 0; i < size; i++)
        {
            for (j = 0; j < size; j++)
            {
                if (9 == grid[i*size + j])
                {
                    System.arraycopy (grid, 0, new_grid, 0, size*size);
                    if (true == markInGrid (new_grid, size, i, j, 1))
                    {
                        /* only if we have finished more than 5 moves,
                            we need check if any player has won */
                        if ((num_moves + 1) >= (2*size - 1))
                        {
                            int result = evaluateGrid (new_grid, size, num_moves + 1);

                            /* if no result yet, then go a level
                                more in search tree */
                            if (9 == result)
                            {
                                temp_move = maxMove (new_grid, size, num_moves + 1);
                            }else
                            {
                                temp_move.setMove(i, j, result);
                            }

                        }else
                        {
                            temp_move = maxMove (new_grid, size, num_moves + 1);
                        }


                        if (temp_move.val < best_move.val)
                        {
                            best_move.val = temp_move.val;
                            best_move.row = i;
                            best_move.col = j;
                        }
                    }
                }
            }
        }

        return best_move;
    }
   

    public static void main (String[] args)
    {
        int size = 3;
        TicTacToe obj = new TicTacToe (size);
        Boolean done = false;
        BufferedReader stdin =	new BufferedReader(new InputStreamReader(System.in));
		String str = null;
        String strNum[];
        int row, column;
        int num_moves = 0;
        int result;
        int []rc;
        Boolean invalid_move;

        System.out.println("Instructions:");
        System.out.println("Please specify the Row and Column number " +
                            "in row,column fasion\n" +
                            "example: 3,3 (row and column number start from 1 not 0)\n");

        obj.displayGrid();

        while (true != done)
        {                    
            System.out.print("\nPlayer 1 has to make a move:\n");

            invalid_move = true;

            while (true == invalid_move)
            {
                try {
                    str = stdin.readLine();
                } catch (IOException e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                strNum = str.split(",");

                row = Integer.parseInt(strNum[0]) - 1;
                column = Integer.parseInt(strNum[1]) - 1;

                if(false == obj.markInGrid(row, column, 0))
                {
                    System.out.println ("Invalid Move");
                    obj.displayGrid();
                }else
                {
                    num_moves++;
                    invalid_move = false;
                }
            }

            obj.displayGrid();

            if (num_moves >= (2*size - 1))
            {
                result = obj.evaluateGrid(num_moves);

                if (9 != result)
                {
                    if(+1 == result)
                    {
                        System.out.println ("Player 1 has won");
                    }else if (-1 == result)
                    {
                        System.out.println ("Computer has won");
                    }else
                    {
                        System.out.println ("The game is drawn");
                    }
                    done = true;
                    break;
                }
            }

            if (num_moves < (size * size))
            {
                System.out.println ("Computer's turn to move");
                rc = obj.machineMove(1, num_moves);
                obj.markInGrid(rc[0], rc[1], 1);
                obj.displayGrid();
                num_moves++;
            }

            if (num_moves >= (2*size - 1))
            {
                result = obj.evaluateGrid(num_moves);

                if (9 != result)
                {
                    if(+1 == result)
                    {
                        System.out.println ("Player 1 has won");
                    }else if (-1 == result)
                    {
                        System.out.println ("Computer has won");
                    }else
                    {
                        System.out.println ("The game is drawn");
                    }
                    done = true;
                }
            }
            
        }
    }
}

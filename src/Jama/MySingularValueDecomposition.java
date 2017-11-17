package Jama;

import Jama.util.Maths;
import java.io.Serializable;

public class MySingularValueDecomposition
  implements Serializable
{
  private double[][] U;
  private double[][] V;
  private double[] s;
  private int m;
  private int n;
  
  public MySingularValueDecomposition(Matrix paramMatrix)
  {
    double[][] arrayOfDouble = paramMatrix.getArrayCopy();
    this.m = paramMatrix.getRowDimension();
    this.n = paramMatrix.getColumnDimension();
    
    int i = Math.min(this.m, this.n);
    this.s = new double[Math.min(this.m + 1, this.n)];
    this.U = new double[this.m][i];
    this.V = new double[this.n][this.n];
    double[] arrayOfDouble1 = new double[this.n];
    double[] arrayOfDouble2 = new double[this.m];
    int j = 1;
    int k = 1;
    
    int i1 = Math.min(this.m - 1, this.n);
    int i2 = Math.max(0, Math.min(this.n - 2, this.m));
    for (int i3 = 0; i3 < Math.max(i1, i2); i3++)
    {
      if (i3 < i1)
      {
        this.s[i3] = 0.0D;
        for (i4 = i3; i4 < this.m; i4++) {
          this.s[i3] = Maths.hypot(this.s[i3], arrayOfDouble[i4][i3]);
        }
        if (this.s[i3] != 0.0D)
        {
          if (arrayOfDouble[i3][i3] < 0.0D) {
            this.s[i3] = (-this.s[i3]);
          }
          for (i4 = i3; i4 < this.m; i4++) {
            arrayOfDouble[i4][i3] /= this.s[i3];
          }
          arrayOfDouble[i3][i3] += 1.0D;
        }
        this.s[i3] = (-this.s[i3]);
      }
      int i7;
      for (i4 = i3 + 1; i4 < this.n; i4++)
      {
        if (((i3 < i1 ? 1 : 0) & (this.s[i3] != 0.0D ? 1 : 0)) != 0)
        {
          double d1 = 0.0D;
          for (i7 = i3; i7 < this.m; i7++) {
            d1 += arrayOfDouble[i7][i3] * arrayOfDouble[i7][i4];
          }
          d1 = -d1 / arrayOfDouble[i3][i3];
          for (i7 = i3; i7 < this.m; i7++) {
            arrayOfDouble[i7][i4] += d1 * arrayOfDouble[i7][i3];
          }
        }
        arrayOfDouble1[i4] = arrayOfDouble[i3][i4];
      }
      if ((j & (i3 < i1 ? 1 : 0)) != 0) {
        for (i4 = i3; i4 < this.m; i4++) {
          this.U[i4][i3] = arrayOfDouble[i4][i3];
        }
      }
      if (i3 < i2)
      {
        arrayOfDouble1[i3] = 0.0D;
        for (i4 = i3 + 1; i4 < this.n; i4++) {
          arrayOfDouble1[i3] = Maths.hypot(arrayOfDouble1[i3], arrayOfDouble1[i4]);
        }
        if (arrayOfDouble1[i3] != 0.0D)
        {
          if (arrayOfDouble1[(i3 + 1)] < 0.0D) {
            arrayOfDouble1[i3] = (-arrayOfDouble1[i3]);
          }
          for (i4 = i3 + 1; i4 < this.n; i4++) {
            arrayOfDouble1[i4] /= arrayOfDouble1[i3];
          }
          arrayOfDouble1[(i3 + 1)] += 1.0D;
        }
        arrayOfDouble1[i3] = (-arrayOfDouble1[i3]);
        if (((i3 + 1 < this.m ? 1 : 0) & (arrayOfDouble1[i3] != 0.0D ? 1 : 0)) != 0)
        {
          for (i4 = i3 + 1; i4 < this.m; i4++) {
            arrayOfDouble2[i4] = 0.0D;
          }
          for (i4 = i3 + 1; i4 < this.n; i4++) {
            for (int i5 = i3 + 1; i5 < this.m; i5++) {
              arrayOfDouble2[i5] += arrayOfDouble1[i4] * arrayOfDouble[i5][i4];
            }
          }
          for (i4 = i3 + 1; i4 < this.n; i4++)
          {
            double d2 = -arrayOfDouble1[i4] / arrayOfDouble1[(i3 + 1)];
            for (i7 = i3 + 1; i7 < this.m; i7++) {
              arrayOfDouble[i7][i4] += d2 * arrayOfDouble2[i7];
            }
          }
        }
        if (k != 0) {
          for (i4 = i3 + 1; i4 < this.n; i4++) {
            this.V[i4][i3] = arrayOfDouble1[i4];
          }
        }
      }
    }
    i3 = Math.min(this.n, this.m + 1);
    if (i1 < this.n) {
      this.s[i1] = arrayOfDouble[i1][i1];
    }
    if (this.m < i3) {
      this.s[(i3 - 1)] = 0.0D;
    }
    if (i2 + 1 < i3) {
      arrayOfDouble1[i2] = arrayOfDouble[i2][(i3 - 1)];
    }
    arrayOfDouble1[(i3 - 1)] = 0.0D;
    int i8;
    if (j != 0)
    {
      for (i4 = i1; i4 < i; i4++)
      {
        for (i6 = 0; i6 < this.m; i6++) {
          this.U[i6][i4] = 0.0D;
        }
        this.U[i4][i4] = 1.0D;
      }
      for (i4 = i1 - 1; i4 >= 0; i4--) {
        if (this.s[i4] != 0.0D)
        {
          for (i6 = i4 + 1; i6 < i; i6++)
          {
            d3 = 0.0D;
            for (i8 = i4; i8 < this.m; i8++) {
              d3 += this.U[i8][i4] * this.U[i8][i6];
            }
            d3 = -d3 / this.U[i4][i4];
            for (i8 = i4; i8 < this.m; i8++) {
              this.U[i8][i6] += d3 * this.U[i8][i4];
            }
          }
          for (i6 = i4; i6 < this.m; i6++) {
            this.U[i6][i4] = (-this.U[i6][i4]);
          }
          this.U[i4][i4] = (1.0D + this.U[i4][i4]);
          for (i6 = 0; i6 < i4 - 1; i6++) {
            this.U[i6][i4] = 0.0D;
          }
        }
        else
        {
          for (i6 = 0; i6 < this.m; i6++) {
            this.U[i6][i4] = 0.0D;
          }
          this.U[i4][i4] = 1.0D;
        }
      }
    }
    if (k != 0) {
      for (i4 = this.n - 1; i4 >= 0; i4--)
      {
        if (((i4 < i2 ? 1 : 0) & (arrayOfDouble1[i4] != 0.0D ? 1 : 0)) != 0) {
          for (i6 = i4 + 1; i6 < i; i6++)
          {
            d3 = 0.0D;
            for (i8 = i4 + 1; i8 < this.n; i8++) {
              d3 += this.V[i8][i4] * this.V[i8][i6];
            }
            d3 = -d3 / this.V[(i4 + 1)][i4];
            for (i8 = i4 + 1; i8 < this.n; i8++) {
              this.V[i8][i6] += d3 * this.V[i8][i4];
            }
          }
        }
        for (i6 = 0; i6 < this.n; i6++) {
          this.V[i6][i4] = 0.0D;
        }
        this.V[i4][i4] = 1.0D;
      }
    }
    int i4 = i3 - 1;
    int i6 = 0;
    double d3 = Math.pow(2.0D, -52.0D);
    double d4 = Math.pow(2.0D, -966.0D);
    while (i3 > 0)
    {
      for (int i9 = i3 - 2; i9 >= -1; i9--)
      {
        if (i9 == -1) {
          break;
        }
        if (Math.abs(arrayOfDouble1[i9]) <= d4 + d3 * (Math.abs(this.s[i9]) + Math.abs(this.s[(i9 + 1)])))
        {
          arrayOfDouble1[i9] = 0.0D;
          break;
        }
      }
      int i10;
      if (i9 == i3 - 2)
      {
        i10 = 4;
      }
      else
      {
        for (int i11 = i3 - 1; i11 >= i9; i11--)
        {
          if (i11 == i9) {
            break;
          }
          double d7 = (i11 != i3 ? Math.abs(arrayOfDouble1[i11]) : 0.0D) + (i11 != i9 + 1 ? Math.abs(arrayOfDouble1[(i11 - 1)]) : 0.0D);
          if (Math.abs(this.s[i11]) <= d4 + d3 * d7)
          {
            this.s[i11] = 0.0D;
            break;
          }
        }
        if (i11 == i9)
        {
          i10 = 3;
        }
        else if (i11 == i3 - 1)
        {
          i10 = 1;
        }
        else
        {
          i10 = 2;
          i9 = i11;
        }
      }
      i9++;
      double d5;
      int i13;
      double d9;
      double d11;
      double d13;
      int i15;
      switch (i10)
      {
      case 1: 
        d5 = arrayOfDouble1[(i3 - 2)];
        arrayOfDouble1[(i3 - 2)] = 0.0D;
        for (i13 = i3 - 2; i13 >= i9; i13--)
        {
          d9 = Maths.hypot(this.s[i13], d5);
          d11 = this.s[i13] / d9;
          d13 = d5 / d9;
          this.s[i13] = d9;
          if (i13 != i9)
          {
            d5 = -d13 * arrayOfDouble1[(i13 - 1)];
            arrayOfDouble1[(i13 - 1)] = (d11 * arrayOfDouble1[(i13 - 1)]);
          }
          if (k != 0) {
            for (i15 = 0; i15 < this.n; i15++)
            {
              d9 = d11 * this.V[i15][i13] + d13 * this.V[i15][(i3 - 1)];
              this.V[i15][(i3 - 1)] = (-d13 * this.V[i15][i13] + d11 * this.V[i15][(i3 - 1)]);
              this.V[i15][i13] = d9;
            }
          }
        }
        break;
      case 2: 
        d5 = arrayOfDouble1[(i9 - 1)];
        arrayOfDouble1[(i9 - 1)] = 0.0D;
        for (i13 = i9; i13 < i3; i13++)
        {
          d9 = Maths.hypot(this.s[i13], d5);
          d11 = this.s[i13] / d9;
          d13 = d5 / d9;
          this.s[i13] = d9;
          d5 = -d13 * arrayOfDouble1[i13];
          arrayOfDouble1[i13] = (d11 * arrayOfDouble1[i13]);
          if (j != 0) {
            for (i15 = 0; i15 < this.m; i15++)
            {
              d9 = d11 * this.U[i15][i13] + d13 * this.U[i15][(i9 - 1)];
              this.U[i15][(i9 - 1)] = (-d13 * this.U[i15][i13] + d11 * this.U[i15][(i9 - 1)]);
              this.U[i15][i13] = d9;
            }
          }
        }
        break;
      case 3: 
        d5 = Math.max(Math.max(Math.max(Math.max(Math.abs(this.s[(i3 - 1)]), Math.abs(this.s[(i3 - 2)])), Math.abs(arrayOfDouble1[(i3 - 2)])), Math.abs(this.s[i9])), Math.abs(arrayOfDouble1[i9]));
        
        double d8 = this.s[(i3 - 1)] / d5;
        double d10 = this.s[(i3 - 2)] / d5;
        double d12 = arrayOfDouble1[(i3 - 2)] / d5;
        double d14 = this.s[i9] / d5;
        double d15 = arrayOfDouble1[i9] / d5;
        double d16 = ((d10 + d8) * (d10 - d8) + d12 * d12) / 2.0D;
        double d17 = d8 * d12 * (d8 * d12);
        double d18 = 0.0D;
        if (((d16 != 0.0D ? 1 : 0) | (d17 != 0.0D ? 1 : 0)) != 0)
        {
          d18 = Math.sqrt(d16 * d16 + d17);
          if (d16 < 0.0D) {
            d18 = -d18;
          }
          d18 = d17 / (d16 + d18);
        }
        double d19 = (d14 + d8) * (d14 - d8) + d18;
        double d20 = d14 * d15;
        for (int i16 = i9; i16 < i3 - 1; i16++)
        {
          double d21 = Maths.hypot(d19, d20);
          double d22 = d19 / d21;
          double d23 = d20 / d21;
          if (i16 != i9) {
            arrayOfDouble1[(i16 - 1)] = d21;
          }
          d19 = d22 * this.s[i16] + d23 * arrayOfDouble1[i16];
          arrayOfDouble1[i16] = (d22 * arrayOfDouble1[i16] - d23 * this.s[i16]);
          d20 = d23 * this.s[(i16 + 1)];
          this.s[(i16 + 1)] = (d22 * this.s[(i16 + 1)]);
          int i17;
          if (k != 0) {
            for (i17 = 0; i17 < this.n; i17++)
            {
              d21 = d22 * this.V[i17][i16] + d23 * this.V[i17][(i16 + 1)];
              this.V[i17][(i16 + 1)] = (-d23 * this.V[i17][i16] + d22 * this.V[i17][(i16 + 1)]);
              this.V[i17][i16] = d21;
            }
          }
          d21 = Maths.hypot(d19, d20);
          d22 = d19 / d21;
          d23 = d20 / d21;
          this.s[i16] = d21;
          d19 = d22 * arrayOfDouble1[i16] + d23 * this.s[(i16 + 1)];
          this.s[(i16 + 1)] = (-d23 * arrayOfDouble1[i16] + d22 * this.s[(i16 + 1)]);
          d20 = d23 * arrayOfDouble1[(i16 + 1)];
          arrayOfDouble1[(i16 + 1)] = (d22 * arrayOfDouble1[(i16 + 1)]);
          if ((j != 0) && (i16 < this.m - 1)) {
            for (i17 = 0; i17 < this.m; i17++)
            {
              d21 = d22 * this.U[i17][i16] + d23 * this.U[i17][(i16 + 1)];
              this.U[i17][(i16 + 1)] = (-d23 * this.U[i17][i16] + d22 * this.U[i17][(i16 + 1)]);
              this.U[i17][i16] = d21;
            }
          }
        }
        arrayOfDouble1[(i3 - 2)] = d19;
        i6 += 1;
        
        break;
      case 4: 
        if (this.s[i9] <= 0.0D)
        {
          this.s[i9] = (this.s[i9] < 0.0D ? -this.s[i9] : 0.0D);
          if (k != 0) {
            for (int i12 = 0; i12 <= i4; i12++) {
              this.V[i12][i9] = (-this.V[i12][i9]);
            }
          }
        }
        while ((i9 < i4) && 
          (this.s[i9] < this.s[(i9 + 1)]))
        {
          double d6 = this.s[i9];
          this.s[i9] = this.s[(i9 + 1)];
          this.s[(i9 + 1)] = d6;
          int i14;
          if ((k != 0) && (i9 < this.n - 1)) {
            for (i14 = 0; i14 < this.n; i14++)
            {
              d6 = this.V[i14][(i9 + 1)];this.V[i14][(i9 + 1)] = this.V[i14][i9];this.V[i14][i9] = d6;
            }
          }
          if ((j != 0) && (i9 < this.m - 1)) {
            for (i14 = 0; i14 < this.m; i14++)
            {
              d6 = this.U[i14][(i9 + 1)];this.U[i14][(i9 + 1)] = this.U[i14][i9];this.U[i14][i9] = d6;
            }
          }
          i9++;
        }
        i6 = 0;
        i3--;
      }
    }
  }
  
  public Matrix getU()
  {
    //return new Matrix(this.U, this.m, Math.min(this.m + 1, this.n));
	  return new Matrix(U);
  }
  
  public Matrix getV()
  {
    return new Matrix(this.V, this.n, this.n);
  }
  
  public double[] getSingularValues()
  {
    return this.s;
  }
  
  public Matrix getS()
  {
    Matrix localMatrix = new Matrix(this.n, this.n);
    double[][] arrayOfDouble = localMatrix.getArray();
    for (int i = 0; i < this.n; i++)
    {
      for (int j = 0; j < this.n; j++) {
        arrayOfDouble[i][j] = 0.0D;
      }
      arrayOfDouble[i][i] = this.s[i];
    }
    return localMatrix;
  }
  
  public double norm2()
  {
    return this.s[0];
  }
  
  public double cond()
  {
    return this.s[0] / this.s[(Math.min(this.m, this.n) - 1)];
  }
  
  public int rank()
  {
    double d1 = Math.pow(2.0D, -52.0D);
    double d2 = Math.max(this.m, this.n) * this.s[0] * d1;
    int i = 0;
    for (int j = 0; j < this.s.length; j++) {
      if (this.s[j] > d2) {
        i++;
      }
    }
    return i;
  }
}

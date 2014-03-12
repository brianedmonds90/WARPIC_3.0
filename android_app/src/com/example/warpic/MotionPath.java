package com.example.warpic;

import java.util.ArrayList;

public class MotionPath {
	String name;
	ArrayList<Pt> A,B,C,D;
	public Pt prevTouch;
	public Pt currentTouch;
	public Pt displacement;
	public Pt prevTouch_1;
	public String unparsed_string;
	public Pt upperLeftCorner, bottomLeftCorner, bottomRightCorner,upperRightCorner;
	double ellapsed_time;
	public MotionPath(String n){
		name=n;
		A= new ArrayList<Pt>();
		B= new ArrayList<Pt>();
		C= new ArrayList<Pt>();
		D= new ArrayList<Pt>();
		prevTouch=new Pt(0,0);
		currentTouch= new Pt(0,0);
		displacement= new Pt(0,0);
		upperLeftCorner= new Pt();
		upperRightCorner = new Pt();
		bottomLeftCorner = new Pt();
		bottomRightCorner = new Pt();
		
	}
	
	public String toSting(){
		return name;
	}
	
//	public void createBoundingBox(){
//		Pt left,upper,lower,right;
//		left = findLeftMostX(A,B,C,D);
//	}
//	
//	private Pt findLeftMostX(ArrayList<Pt> a2, ArrayList<Pt> b2,
//			ArrayList<Pt> c2, ArrayList<Pt> d2) {
//		Pt ret = new 
//		return null;
//	}

	public void showPaths(WarpicActivity wp){
		draw(A,wp);
		draw(B,wp);
		draw(C,wp);
		draw(D,wp);
	}
	
	void draw(ArrayList<Pt> list, WarpicActivity wp) {
		    wp.beginShape(); 
		    for (int v=0; v<list.size(); v++){
		      list.get(v).v(wp);
		    }
		    wp.endShape();
	}
	
	public float computeLeftMostBound(){
		float leftMost=9999999f;
		for(Pt p: A){
			if(p.x<leftMost)
				leftMost=p.x;
		}
		for(Pt p: B){
			if(p.x<leftMost)
				leftMost=p.x;
		}
		for(Pt p: C){
			if(p.x<leftMost)
				leftMost=p.x;
		}
		for(Pt p: D){
			if(p.x<leftMost)
				leftMost=p.x;
		}
		return leftMost;
	}
	
	public float computeRightMostBound(){
		float rightMost=-9999999f;
		for(Pt p: A){
			if(p.x>rightMost)
				rightMost=p.x;
		}
		for(Pt p: B){
			if(p.x>rightMost)
				rightMost=p.x;
		}
		for(Pt p: C){
			if(p.x>rightMost)
				rightMost=p.x;
		}
		for(Pt p: D){
			if(p.x>rightMost)
				rightMost=p.x;
		}
		return rightMost;
	}
	
	/********************************************* Smoothing ***************************/
	void smooth(){
		smooth(A);
		smooth(B);
		smooth(C);
		smooth(D);
	}
	void smooth(ArrayList<Pt> l) {
		for (int k = 0; k < 4; k++) {
			tuck((float) .5, l);
			tuck((float) -.5, l);
		}
	}

	void tuck(float s, ArrayList<Pt> list) {
		Pt[] S = new Pt[list.size()]; // temporry array
		// copy of each intermediate point moved by s towards the average of its
		// neighbors
		for (int v = 1; v < list.size() - 1; v++)
			S[v] = MathUtility.P(list.get(v), s, MathUtility.average(list.get(v - 1), list.get(v + 1))); // S
																					// =
																					// G
																					// +
																					// s((P+N)/2-G)
		for (int v = 1; v < list.size() - 1; v++)
			list.set(v, S[v]); // copy back
	}
	
	
	public float computeUpperMostBound(){
		float upperMost=-9999999f;
		for(Pt p: A){
			if(p.y>upperMost)
				upperMost=p.y;
		}
		for(Pt p: B){
			if(p.y>upperMost)
				upperMost=p.y;
		}
		for(Pt p: C){
			if(p.y>upperMost)
				upperMost=p.y;
		}
		for(Pt p: D){
			if(p.y>upperMost)
				upperMost=p.y;
		}
		return upperMost;
	}
	
	public float computeLowerMostBound(){
		float lowerMost=9999999f;
		for(Pt p: A){
			if(p.y<lowerMost)
				lowerMost=p.y;
		}
		for(Pt p: B){
			if(p.y<lowerMost)
				lowerMost=p.y;
		}
		for(Pt p: C){
			if(p.y<lowerMost)
				lowerMost=p.y;
		}
		for(Pt p: D){
			if(p.y<lowerMost)
				lowerMost=p.y;
		}
		return lowerMost;
	}
	
	public void drawBoundingBox(WarpicActivity wp){
		wp.fill(255,0,0);
		wp.stroke(255,0,0);
		bottomLeftCorner.show(bottomLeftCorner, 10, wp);
		wp.noFill();
		wp.beginShape();
		wp.vertex(upperLeftCorner.x,upperLeftCorner.y);
		wp.vertex(upperRightCorner.x,upperRightCorner.y);
		wp.vertex(bottomRightCorner.x,bottomRightCorner.y);
		wp.vertex(bottomLeftCorner.x,bottomLeftCorner.y);
		wp.vertex(upperLeftCorner.x,upperLeftCorner.y);
		wp.endShape();
	}
	
	public void getBoundingBoxCoords(){
		upperLeftCorner=new Pt(computeLeftMostBound(),computeUpperMostBound());
		upperRightCorner= new Pt(computeRightMostBound(),computeUpperMostBound());
		bottomRightCorner = new Pt(computeRightMostBound(),computeLowerMostBound());
		bottomLeftCorner = new Pt(computeLeftMostBound(),computeLowerMostBound());
	}
	
	public void initSmile(){
		
		A.add(new Pt(854.375f,570.60547f));
		A.add(new Pt(854.3403f,570.60046f));
		A.add(new Pt(854.3557f,570.6062f));
		A.add(new Pt(854.5349f,570.6339f));
		A.add(new Pt(854.89075f,570.64526f));
		A.add(new Pt(855.2815f,570.62085f));
		A.add(new Pt(855.63763f,570.80493f));
		A.add(new Pt(856.03125f,571.59045f));
		A.add(new Pt(856.4702f,572.89905f));
		A.add(new Pt(856.8608f,574.12256f));
		A.add(new Pt(857.20013f,574.89526f));
		A.add(new Pt(857.5877f,575.3839f));
		A.add(new Pt(858.0439f,575.799f));
		A.add(new Pt(858.4763f,576.14966f));
		A.add(new Pt(858.8013f,576.41156f));
		A.add(new Pt(859.01483f,576.6186f));
		A.add(new Pt(859.17175f,576.8236f));
		A.add(new Pt(859.2975f,577.04364f));
		A.add(new Pt(859.3617f,577.22424f));
		A.add(new Pt(859.38794f,577.3342f));
		A.add(new Pt(859.46625f,577.46924f));
		A.add(new Pt(859.59796f,577.7222f));
		A.add(new Pt(859.6885f,578.04144f));
		A.add(new Pt(859.70294f,578.3493f));
		A.add(new Pt(859.69006f,578.6768f));
		A.add(new Pt(859.68396f,579.0913f));
		A.add(new Pt(859.693f,579.60156f));
		A.add(new Pt(859.7168f,580.1738f));
		A.add(new Pt(859.69275f,580.7566f));
		A.add(new Pt(859.52606f,581.279f));
		A.add(new Pt(859.24f,581.7098f));
		A.add(new Pt(858.9713f,582.0973f));
		A.add(new Pt(858.81274f,582.4895f));
		A.add(new Pt(858.756f,582.852f));
		A.add(new Pt(858.7238f,583.1301f));
		A.add(new Pt(858.63635f,583.3485f));
		A.add(new Pt(858.51575f,583.55597f));
		A.add(new Pt(858.44934f,583.7302f));
		A.add(new Pt(858.4242f,583.85547f));
		A.add(new Pt(858.3498f,583.9892f));
		A.add(new Pt(858.22815f,584.1703f));
		A.add(new Pt(858.12573f,584.3745f));
		A.add(new Pt(858.03986f,584.57117f));
		A.add(new Pt(857.92725f,584.74115f));
		A.add(new Pt(857.75964f,584.8857f));
		A.add(new Pt(857.4832f,585.1569f));
		A.add(new Pt(856.9818f,585.9705f));
		A.add(new Pt(856.13885f,587.6214f));
		A.add(new Pt(854.98975f,589.6918f));
		A.add(new Pt(853.67804f,591.4269f));
		A.add(new Pt(852.2803f,592.66254f));
		A.add(new Pt(850.88306f,593.7036f));
		A.add(new Pt(849.5878f,594.65967f));
		A.add(new Pt(848.3563f,595.4796f));
		A.add(new Pt(847.1218f,596.19525f));
		A.add(new Pt(845.78357f,596.89075f));
		A.add(new Pt(844.1013f,597.718f));
		A.add(new Pt(841.98987f,599.0246f));
		A.add(new Pt(839.6504f,601.21216f));
		A.add(new Pt(837.2821f,604.1426f));
		A.add(new Pt(834.864f,607.06775f));
		A.add(new Pt(832.2395f,609.4453f));
		A.add(new Pt(829.46094f,611.22095f));
		A.add(new Pt(826.7254f,612.4546f));
		A.add(new Pt(824.025f,613.3121f));
		A.add(new Pt(821.4126f,614.187f));
		A.add(new Pt(819.07056f,615.59076f));
		A.add(new Pt(816.83954f,617.74194f));
		A.add(new Pt(814.4359f,620.147f));
		A.add(new Pt(811.8636f,622.10675f));
		A.add(new Pt(809.2102f,623.5191f));
		A.add(new Pt(806.6156f,624.62244f));
		A.add(new Pt(804.37036f,625.4466f));
		A.add(new Pt(802.4966f,625.97595f));
		A.add(new Pt(800.58405f,626.37585f));
		A.add(new Pt(798.4042f,626.782f));
		A.add(new Pt(796.23083f,627.1309f));
		A.add(new Pt(794.3977f,627.3192f));
		A.add(new Pt(792.9164f,627.3641f));
		A.add(new Pt(791.58777f,627.354f));
		A.add(new Pt(790.3157f,627.34534f));
		A.add(new Pt(789.19434f,627.3406f));
		A.add(new Pt(788.3104f,627.3291f));
		A.add(new Pt(787.6083f,627.33716f));
		A.add(new Pt(786.9459f,627.417f));
		A.add(new Pt(786.22974f,627.5717f));
		A.add(new Pt(785.49365f,627.7549f));
		A.add(new Pt(784.8439f,627.9425f));
		A.add(new Pt(784.3688f,628.1251f));
		A.add(new Pt(784.07874f,628.2726f));
		A.add(new Pt(783.90515f,628.37616f));
		A.add(new Pt(783.7941f,628.4543f));
		A.add(new Pt(783.74316f,628.5049f));
		A.add(new Pt(783.7399f,628.52563f));
		A.add(new Pt(783.74854f,628.5387f));
		A.add(new Pt(783.75165f,628.526f));
		A.add(new Pt(783.7506f,628.4197f));
		A.add(new Pt(783.7384f,628.2103f));
		A.add(new Pt(783.7261f,627.9429f));
		A.add(new Pt(783.7893f,627.6203f));
		A.add(new Pt(783.98987f,627.2579f));
		A.add(new Pt(784.24005f,626.9289f));
		A.add(new Pt(784.38403f,626.6188f));
		A.add(new Pt(784.39935f,626.2128f));
		A.add(new Pt(784.37665f,625.71704f));
		A.add(new Pt(784.37897f,625.25085f));
		A.add(new Pt(784.405f,624.818f));
		A.add(new Pt(784.3767f,624.33606f));
		A.add(new Pt(784.19946f,623.83356f));
		A.add(new Pt(783.93604f,623.43286f));
		A.add(new Pt(783.7569f,623.19037f));
		A.add(new Pt(783.6794f,623.021f));
		A.add(new Pt(783.57983f,622.7927f));
		A.add(new Pt(783.4132f,622.4889f));
		A.add(new Pt(783.20966f,622.18695f));
		A.add(new Pt(782.96704f,621.9186f));
		A.add(new Pt(782.69556f,621.6658f));
		A.add(new Pt(782.4325f,621.4018f));
		A.add(new Pt(782.17444f,621.09796f));
		A.add(new Pt(781.9058f,620.78845f));
		A.add(new Pt(781.61505f,620.44775f));
		A.add(new Pt(781.24365f,619.74774f));
		A.add(new Pt(780.75977f,618.32043f));
		A.add(new Pt(780.249f,616.37695f));
		A.add(new Pt(779.79767f,614.662f));
		A.add(new Pt(779.3336f,613.6614f));
		A.add(new Pt(778.71716f,613.21533f));
		A.add(new Pt(777.96655f,612.9922f));
		A.add(new Pt(777.20404f,612.8773f));
		A.add(new Pt(776.41003f,612.8301f));
		A.add(new Pt(775.47253f,612.7718f));
		A.add(new Pt(774.39075f,612.6407f));
		A.add(new Pt(773.2499f,612.3529f));
		A.add(new Pt(772.11145f,611.8269f));
		A.add(new Pt(771.03076f,611.1111f));
		A.add(new Pt(770.1068f,610.193f));
		A.add(new Pt(769.43066f,608.71704f));
		A.add(new Pt(769.011f,606.46576f));
		A.add(new Pt(768.79785f,604.04694f));
		A.add(new Pt(768.7307f,602.379f));
		A.add(new Pt(768.7372f,601.6355f));
		A.add(new Pt(768.74286f,601.28577f));
		A.add(new Pt(768.7219f,600.89685f));
		A.add(new Pt(768.74493f,600.431f));
		A.add(new Pt(768.91125f,599.9922f));
		A.add(new Pt(769.19714f,599.61945f));
		A.add(new Pt(769.4657f,599.30286f));
		A.add(new Pt(769.6296f,599.05054f));
		A.add(new Pt(769.69476f,598.86566f));
		A.add(new Pt(769.70154f,598.70386f));
		A.add(new Pt(769.6871f,598.53345f));
		A.add(new Pt(769.6711f,598.38354f));
		A.add(new Pt(769.6864f,598.2887f));
		A.add(new Pt(769.7771f,598.23193f));
		A.add(new Pt(769.9086f,598.1439f));
		A.add(new Pt(769.9861f,597.9665f));
		A.add(new Pt(770.0153f,597.74097f));
		A.add(new Pt(770.0884f,597.55884f));
		A.add(new Pt(770.2078f,597.421f));
		A.add(new Pt(770.31226f,597.2577f));
		A.add(new Pt(770.4161f,597.06573f));
		A.add(new Pt(770.5396f,596.9198f));
		A.add(new Pt(770.625f,596.86316f));
		A.add(new Pt(770.63995f,596.8644f));
		A.add(new Pt(770.6283f,596.87396f));
		A.add(new Pt(770.625f,596.8758f));
		A.add(new Pt(770.62177f,596.8751f));
		A.add(new Pt(770.60986f,596.875f));
		A.add(new Pt(770.62384f,596.875f));
		A.add(new Pt(770.7134f,596.875f));
		A.add(new Pt(770.8491f,596.875f));
		A.add(new Pt(770.93866f,596.875f));
		A.add(new Pt(770.95276f,596.875f));
		A.add(new Pt(770.9404f,596.87494f));
		A.add(new Pt(770.9363f,596.87524f));
		A.add(new Pt(770.93726f,596.87646f));
		A.add(new Pt(770.93756f,596.87134f));
		A.add(new Pt(770.9375f,596.8559f));
		A.add(new Pt(770.9375f,596.87354f));
		A.add(new Pt(770.9375f,596.9854f));
		A.add(new Pt(770.9375f,597.1552f));
		A.add(new Pt(770.9375f,597.2671f));
		A.add(new Pt(770.9375f,597.2847f));
		A.add(new Pt(770.9375f,597.2693f));
		A.add(new Pt(770.9375f,597.26416f));
		A.add(new Pt(770.9375f,597.2654f));
		A.add(new Pt(770.9375f,597.2657f));
		A.add(new Pt(770.93756f,597.2656f));
		A.add(new Pt(770.93884f,597.2656f));
		A.add(new Pt(770.9357f,597.2656f));
		A.add(new Pt(770.9193f,597.2656f));
		A.add(new Pt(770.92114f,597.2656f));
		A.add(new Pt(771.02466f,597.2656f));
		A.add(new Pt(771.25f,597.2656f));
		
B.add(new Pt(1059.6875f,612.3047f));
B.add(new Pt(1059.6874f,612.33466f));
B.add(new Pt(1059.688f,612.4679f));
B.add(new Pt(1059.69f,612.3518f));
B.add(new Pt(1059.6816f,611.40625f));
B.add(new Pt(1059.657f,609.7981f));
B.add(new Pt(1059.6853f,608.44714f));
B.add(new Pt(1059.8655f,607.787f));
B.add(new Pt(1060.1335f,607.47833f));
B.add(new Pt(1060.2971f,607.2241f));
B.add(new Pt(1060.3307f,607.00415f));
B.add(new Pt(1060.4128f,606.82117f));
B.add(new Pt(1060.6296f,606.68604f));
B.add(new Pt(1060.8545f,606.62964f));
B.add(new Pt(1060.9558f,606.63f));
B.add(new Pt(1060.9559f,606.6395f));
B.add(new Pt(1060.939f,606.6419f));
B.add(new Pt(1060.9362f,606.6412f));
B.add(new Pt(1060.937f,606.63525f));
B.add(new Pt(1060.9353f,606.6244f));
B.add(new Pt(1060.9434f,606.644f));
B.add(new Pt(1060.9678f,606.75745f));
B.add(new Pt(1060.9387f,607.0011f));
B.add(new Pt(1060.763f,607.3605f));
B.add(new Pt(1060.5039f,607.81934f));
B.add(new Pt(1060.3218f,608.38983f));
B.add(new Pt(1060.2175f,609.0911f));
B.add(new Pt(1060.0591f,609.91187f));
B.add(new Pt(1059.794f,610.7694f));
B.add(new Pt(1059.4513f,611.5537f));
B.add(new Pt(1059.0208f,612.2485f));
B.add(new Pt(1058.444f,612.9005f));
B.add(new Pt(1057.7366f,613.4947f));
B.add(new Pt(1057.04f,614.0021f));
B.add(new Pt(1056.3385f,614.5914f));
B.add(new Pt(1055.2461f,615.676f));
B.add(new Pt(1053.5522f,617.49005f));
B.add(new Pt(1051.7617f,619.65826f));
B.add(new Pt(1050.4854f,621.5973f));
B.add(new Pt(1049.6973f,623.12585f));
B.add(new Pt(1049.0874f,624.3153f));
B.add(new Pt(1048.5583f,625.25055f));
B.add(new Pt(1048.082f,626.08624f));
B.add(new Pt(1047.5945f,626.93396f));
B.add(new Pt(1047.0952f,627.93933f));
B.add(new Pt(1046.551f,629.45984f));
B.add(new Pt(1045.758f,631.6743f));
B.add(new Pt(1044.5095f,634.29236f));
B.add(new Pt(1042.8927f,636.89526f));
B.add(new Pt(1041.1051f,639.3884f));
B.add(new Pt(1039.1133f,642.06244f));
B.add(new Pt(1036.9529f,645.03906f));
B.add(new Pt(1034.8259f,647.9568f));
B.add(new Pt(1032.6136f,650.45966f));
B.add(new Pt(1030.1318f,652.4408f));
B.add(new Pt(1027.5918f,653.96564f));
B.add(new Pt(1025.167f,655.44104f));
B.add(new Pt(1022.7711f,657.4019f));
B.add(new Pt(1020.473f,659.891f));
B.add(new Pt(1018.5409f,662.3817f));
B.add(new Pt(1016.97516f,664.48676f));
B.add(new Pt(1015.35016f,666.4315f));
B.add(new Pt(1013.3635f,668.5356f));
B.add(new Pt(1011.14453f,670.87036f));
B.add(new Pt(1008.88794f,673.57f));
B.add(new Pt(1006.64984f,676.57f));
B.add(new Pt(1004.2617f,679.37256f));
B.add(new Pt(1001.35706f,681.6975f));
B.add(new Pt(997.9401f,683.699f));
B.add(new Pt(994.4868f,685.6218f));
B.add(new Pt(991.3265f,687.82153f));
B.add(new Pt(988.5637f,690.4824f));
B.add(new Pt(986.2965f,693.13354f));
B.add(new Pt(984.32294f,695.3007f));
B.add(new Pt(982.09875f,697.3253f));
B.add(new Pt(979.32074f,699.74365f));
B.add(new Pt(976.1848f,702.4911f));
B.add(new Pt(973.1024f,705.1806f));
B.add(new Pt(970.3506f,707.5338f));
B.add(new Pt(967.77576f,709.4313f));
B.add(new Pt(964.9164f,710.9357f));
B.add(new Pt(961.66455f,712.23334f));
B.add(new Pt(958.58295f,713.41254f));
B.add(new Pt(956.2384f,714.4521f));
B.add(new Pt(954.48126f,715.4524f));
B.add(new Pt(952.8729f,716.5985f));
B.add(new Pt(951.37537f,717.8955f));
B.add(new Pt(950.05164f,719.1687f));
B.add(new Pt(948.7272f,720.2555f));
B.add(new Pt(947.371f,721.11365f));
B.add(new Pt(946.08655f,721.88257f));
B.add(new Pt(944.69965f,722.79736f));
B.add(new Pt(943.05255f,723.8921f));
B.add(new Pt(941.2602f,725.0034f));
B.add(new Pt(939.36316f,726.0625f));
B.add(new Pt(937.46924f,727.022f));
B.add(new Pt(935.8673f,727.7434f));
B.add(new Pt(934.42773f,728.2461f));
B.add(new Pt(932.661f,728.6882f));
B.add(new Pt(930.5078f,729.0473f));
B.add(new Pt(928.4263f,729.1684f));
B.add(new Pt(926.7395f,729.02454f));
B.add(new Pt(925.2515f,728.6863f));
B.add(new Pt(923.6666f,728.2016f));
B.add(new Pt(922.0867f,727.6979f));
B.add(new Pt(920.6384f,727.37256f));
B.add(new Pt(919.0803f,727.2671f));
B.add(new Pt(917.2466f,727.30566f));
B.add(new Pt(915.2855f,727.5084f));
B.add(new Pt(913.338f,727.9009f));
B.add(new Pt(911.4695f,728.3946f));
B.add(new Pt(909.68634f,728.93726f));
B.add(new Pt(907.91394f,729.53705f));
B.add(new Pt(906.2552f,730.09564f));
B.add(new Pt(904.955f,730.48645f));
B.add(new Pt(904.0134f,730.752f));
B.add(new Pt(903.256f,731.00525f));
B.add(new Pt(902.6004f,731.2577f));
B.add(new Pt(902.0061f,731.46765f));
B.add(new Pt(901.44745f,731.61145f));
B.add(new Pt(900.91235f,731.66174f));
B.add(new Pt(900.29395f,731.5941f));
B.add(new Pt(899.5055f,731.41797f));
B.add(new Pt(898.66956f,731.18506f));
B.add(new Pt(897.97845f,730.9678f));
B.add(new Pt(897.43896f,730.7951f));
B.add(new Pt(896.89514f,730.64685f));
B.add(new Pt(896.2854f,730.52966f));
B.add(new Pt(895.6981f,730.4624f));
B.add(new Pt(895.1459f,730.4004f));
B.add(new Pt(894.5166f,730.3062f));
B.add(new Pt(893.76953f,730.25037f));
B.add(new Pt(892.9681f,730.3535f));
B.add(new Pt(892.1217f,730.6495f));
B.add(new Pt(891.2114f,731.0365f));
B.add(new Pt(890.3312f,731.38464f));
B.add(new Pt(889.43646f,731.679f));
B.add(new Pt(888.1402f,732.0276f));
B.add(new Pt(886.33875f,732.5125f));
B.add(new Pt(884.59375f,733.0851f));
B.add(new Pt(883.2576f,733.65564f));
B.add(new Pt(881.9592f,734.1874f));
B.add(new Pt(880.419f,734.6847f));
B.add(new Pt(878.8827f,735.2927f));
B.add(new Pt(877.56946f,736.24963f));
B.add(new Pt(876.47046f,737.43335f));
B.add(new Pt(875.5978f,738.3407f));
B.add(new Pt(874.954f,738.73834f));
B.add(new Pt(874.44476f,738.8557f));
B.add(new Pt(873.9679f,738.9324f));
B.add(new Pt(873.4796f,739.0055f));
B.add(new Pt(873.01f,739.0658f));
B.add(new Pt(872.61206f,739.1476f));
B.add(new Pt(872.2434f,739.224f));
B.add(new Pt(871.7751f,739.15186f));
B.add(new Pt(871.15027f,738.87524f));
B.add(new Pt(870.446f,738.60925f));
B.add(new Pt(869.7545f,738.60516f));
B.add(new Pt(869.08325f,738.8135f));
B.add(new Pt(868.41846f,738.9875f));
B.add(new Pt(867.8129f,738.99316f));
B.add(new Pt(867.3164f,738.9292f));
B.add(new Pt(866.8757f,739.0665f));
B.add(new Pt(866.40393f,739.5503f));
B.add(new Pt(865.8662f,740.1808f));
B.add(new Pt(865.25525f,740.6958f));
B.add(new Pt(864.62415f,741.0685f));
B.add(new Pt(864.1015f,741.3814f));
B.add(new Pt(863.7434f,741.6604f));
B.add(new Pt(863.44116f,741.9013f));
B.add(new Pt(863.04016f,742.114f));
B.add(new Pt(862.5084f,742.3175f));
B.add(new Pt(861.9533f,742.5124f));
B.add(new Pt(861.4833f,742.6736f));
B.add(new Pt(861.1486f,742.7883f));
B.add(new Pt(860.9751f,742.8693f));
B.add(new Pt(860.91846f,742.9446f));
B.add(new Pt(860.8563f,743.0566f));
B.add(new Pt(860.71405f,743.20715f));
B.add(new Pt(860.523f,743.324f));
B.add(new Pt(860.35767f,743.3594f));
B.add(new Pt(860.2518f,743.36536f));
B.add(new Pt(860.17004f,743.41516f));
B.add(new Pt(860.0845f,743.49976f));
B.add(new Pt(860.0016f,743.5536f));
B.add(new Pt(859.89935f,743.55554f));
B.add(new Pt(859.7733f,743.5547f));
B.add(new Pt(859.6874f,743.5978f));
B.add(new Pt(859.6725f,743.6844f));
B.add(new Pt(859.6845f,743.8052f));
B.add(new Pt(859.6887f,743.9549f));
B.add(new Pt(859.68774f,744.0888f));
B.add(new Pt(859.68744f,744.149f));
B.add(new Pt(859.6875f,744.1406f));

C.add(new Pt(868.75f,251.95313f));
C.add(new Pt(868.75f,251.95313f));
C.add(new Pt(868.75f,251.95313f));
C.add(new Pt(868.75006f,251.9531f));
C.add(new Pt(868.7489f,251.95345f));
C.add(new Pt(868.75305f,251.95232f));
C.add(new Pt(868.76917f,251.94788f));
C.add(new Pt(868.704f,251.965f));
C.add(new Pt(868.5033f,252.02032f));
C.add(new Pt(868.71625f,251.97186f));
C.add(new Pt(870.16223f,251.58533f));
C.add(new Pt(872.42505f,250.91646f));
C.add(new Pt(873.99274f,250.33228f));
C.add(new Pt(874.3074f,250.03804f));
C.add(new Pt(874.125f,249.92392f));
C.add(new Pt(874.04645f,249.85013f));
C.add(new Pt(874.0581f,249.77847f));
C.add(new Pt(874.06354f,249.70703f));
C.add(new Pt(874.0621f,249.64865f));
C.add(new Pt(874.0602f,249.60631f));
C.add(new Pt(874.06824f,249.54803f));
C.add(new Pt(874.0918f,249.46838f));
C.add(new Pt(874.0679f,249.41405f));
C.add(new Pt(873.9009f,249.40616f));
C.add(new Pt(873.6141f,249.4083f));
C.add(new Pt(873.34985f,249.39417f));
C.add(new Pt(873.1979f,249.4157f));
C.add(new Pt(873.1179f,249.54527f));
C.add(new Pt(873.0266f,249.70427f));
C.add(new Pt(872.91223f,249.6969f));
C.add(new Pt(872.81525f,249.50116f));
C.add(new Pt(872.72595f,249.2941f));
C.add(new Pt(872.6035f,249.14703f));
C.add(new Pt(872.43066f,248.9805f));
C.add(new Pt(872.18646f,248.76904f));
C.add(new Pt(871.8755f,248.51746f));
C.add(new Pt(871.57965f,248.17549f));
C.add(new Pt(871.3498f,247.74194f));
C.add(new Pt(871.088f,247.25818f));
C.add(new Pt(870.7023f,246.71823f));
C.add(new Pt(870.279f,246.15527f));
C.add(new Pt(869.89f,245.63521f));
C.add(new Pt(869.4414f,245.13194f));
C.add(new Pt(868.90424f,244.60852f));
C.add(new Pt(868.3711f,244.07611f));
C.add(new Pt(867.8407f,243.50856f));
C.add(new Pt(867.26526f,242.91994f));
C.add(new Pt(866.71533f,242.44185f));
C.add(new Pt(866.28107f,242.15253f));
C.add(new Pt(865.93164f,241.89867f));
C.add(new Pt(865.5688f,241.38571f));
C.add(new Pt(865.16364f,240.4893f));
C.add(new Pt(864.77496f,239.3764f));
C.add(new Pt(864.4439f,238.294f));
C.add(new Pt(864.1437f,237.39139f));
C.add(new Pt(863.7841f,236.63504f));
C.add(new Pt(863.22314f,235.79898f));
C.add(new Pt(862.3712f,234.71524f));
C.add(new Pt(861.307f,233.48293f));
C.add(new Pt(860.2058f,232.22144f));
C.add(new Pt(859.05884f,230.64407f));
C.add(new Pt(857.648f,228.3089f));
C.add(new Pt(855.9509f,225.41502f));
C.add(new Pt(854.09674f,222.58084f));
C.add(new Pt(851.98706f,219.87894f));
C.add(new Pt(849.588f,216.93634f));
C.add(new Pt(847.02527f,213.50343f));
C.add(new Pt(844.1005f,209.586f));
C.add(new Pt(840.62354f,205.64761f));
C.add(new Pt(836.95715f,202.22397f));
C.add(new Pt(833.56354f,199.13116f));
C.add(new Pt(830.6133f,196.01505f));
C.add(new Pt(828.1823f,193.26526f));
C.add(new Pt(826.19135f,191.3696f));
C.add(new Pt(824.29895f,190.07668f));
C.add(new Pt(822.2656f,188.92087f));
C.add(new Pt(820.1776f,187.8532f));
C.add(new Pt(818.2135f,186.98178f));
C.add(new Pt(816.39526f,186.21964f));
C.add(new Pt(814.49f,185.23943f));
C.add(new Pt(812.1537f,183.59305f));
C.add(new Pt(809.3209f,181.20844f));
C.add(new Pt(806.35657f,178.75406f));
C.add(new Pt(803.681f,176.98183f));
C.add(new Pt(801.41675f,175.90665f));
C.add(new Pt(799.5684f,175.12299f));
C.add(new Pt(798.20215f,174.46542f));
C.add(new Pt(797.23f,173.94885f));
C.add(new Pt(796.4264f,173.52585f));
C.add(new Pt(795.73615f,173.15225f));
C.add(new Pt(795.24396f,172.8468f));
C.add(new Pt(794.91907f,172.62921f));
C.add(new Pt(794.65375f,172.50027f));
C.add(new Pt(794.44946f,172.45265f));
C.add(new Pt(794.3533f,172.44603f));
C.add(new Pt(794.3264f,172.43301f));
C.add(new Pt(794.35345f,172.44902f));
C.add(new Pt(794.53174f,172.58897f));
C.add(new Pt(794.8959f,172.88795f));
C.add(new Pt(795.319f,173.30835f));
C.add(new Pt(795.67786f,173.78395f));
C.add(new Pt(795.978f,174.31102f));
C.add(new Pt(796.27783f,175.12741f));
C.add(new Pt(796.5762f,176.49173f));
C.add(new Pt(796.806f,178.14233f));
C.add(new Pt(796.9279f,179.50403f));
C.add(new Pt(796.9265f,180.42276f));
C.add(new Pt(796.719f,181.16924f));
C.add(new Pt(796.2655f,181.91415f));
C.add(new Pt(795.73193f,182.56975f));
C.add(new Pt(795.3131f,183.01624f));
C.add(new Pt(794.9817f,183.32683f));
C.add(new Pt(794.5972f,183.67369f));
C.add(new Pt(794.1526f,184.06903f));
C.add(new Pt(793.7488f,184.42615f));
C.add(new Pt(793.4173f,184.75868f));
C.add(new Pt(793.1134f,185.11176f));
C.add(new Pt(792.8308f,185.4768f));
C.add(new Pt(792.6017f,185.84761f));
C.add(new Pt(792.40497f,186.2301f));
C.add(new Pt(792.1622f,186.69592f));
C.add(new Pt(791.83685f,187.39368f));
C.add(new Pt(791.46686f,188.31244f));
C.add(new Pt(791.097f,189.2068f));
C.add(new Pt(790.75836f,189.88525f));
C.add(new Pt(790.4434f,190.41953f));
C.add(new Pt(790.0802f,191.04092f));
C.add(new Pt(789.64545f,191.87956f));
C.add(new Pt(789.1894f,192.85512f));
C.add(new Pt(788.7234f,193.79567f));
C.add(new Pt(788.26245f,194.53773f));
C.add(new Pt(787.879f,195.00563f));
C.add(new Pt(787.6022f,195.29193f));
C.add(new Pt(787.3982f,195.55238f));
C.add(new Pt(787.25354f,195.85648f));
C.add(new Pt(787.1826f,196.216f));
C.add(new Pt(787.174f,196.66628f));
C.add(new Pt(787.1848f,197.25673f));
C.add(new Pt(787.1886f,197.97067f));
C.add(new Pt(787.1875f,198.68639f));
C.add(new Pt(787.1864f,199.28586f));
C.add(new Pt(787.1899f,199.76816f));
C.add(new Pt(787.2003f,200.18362f));
C.add(new Pt(787.1929f,200.55504f));
C.add(new Pt(787.1227f,200.92972f));
C.add(new Pt(786.9771f,201.38756f));
C.add(new Pt(786.7934f,201.94838f));
C.add(new Pt(786.63684f,202.53978f));
C.add(new Pt(786.5637f,203.07272f));
C.add(new Pt(786.55945f,203.53561f));
C.add(new Pt(786.53613f,203.99535f));
C.add(new Pt(786.4376f,204.48935f));
C.add(new Pt(786.3136f,204.96198f));
C.add(new Pt(786.24524f,205.3588f));
C.add(new Pt(786.2405f,205.73251f));
C.add(new Pt(786.24243f,206.16853f));
C.add(new Pt(786.22156f,206.7335f));
C.add(new Pt(786.24506f,207.6416f));
C.add(new Pt(786.4126f,209.10837f));
C.add(new Pt(786.6942f,210.82239f));
C.add(new Pt(786.9506f,212.1076f));
C.add(new Pt(787.1297f,212.72696f));
C.add(new Pt(787.28015f,213.00122f));
C.add(new Pt(787.41046f,213.25377f));
C.add(new Pt(787.4996f,213.5427f));
C.add(new Pt(787.58734f,213.7737f));
C.add(new Pt(787.7257f,213.87152f));
C.add(new Pt(787.9009f,213.88593f));
C.add(new Pt(788.05176f,213.92514f));
C.add(new Pt(788.1293f,214.00577f));
C.add(new Pt(788.13904f,214.06302f));
C.add(new Pt(788.1278f,214.0721f));
C.add(new Pt(788.1239f,214.06435f));
C.add(new Pt(788.12476f,214.06177f));
C.add(new Pt(788.12506f,214.06238f));
C.add(new Pt(788.125f,214.06256f));
C.add(new Pt(788.125f,214.0625f));
C.add(new Pt(788.125f,214.0625f));
C.add(new Pt(788.125f,214.0625f));
C.add(new Pt(788.125f,214.0625f));
C.add(new Pt(788.125f,214.0625f));
C.add(new Pt(788.125f,214.0625f));
C.add(new Pt(788.125f,214.0625f));
C.add(new Pt(788.1251f,214.0625f));
C.add(new Pt(788.1246f,214.0625f));
C.add(new Pt(788.1226f,214.0625f));
C.add(new Pt(788.13074f,214.0625f));
C.add(new Pt(788.15576f,214.0625f));
C.add(new Pt(788.131f,214.0625f));
C.add(new Pt(787.9436f,214.0625f));
C.add(new Pt(787.62866f,214.0625f));
C.add(new Pt(787.4539f,214.0625f));
C.add(new Pt(787.698f,214.0625f));
C.add(new Pt(788.32733f,214.0625f));

D.add(new Pt(1075.625f,234.57031f));
D.add(new Pt(1075.5652f,234.36002f));
D.add(new Pt(1075.612f,234.51337f));
D.add(new Pt(1075.9326f,235.63199f));
D.add(new Pt(1076.4692f,237.5597f));
D.add(new Pt(1076.9221f,239.29755f));
D.add(new Pt(1077.1512f,240.34612f));
D.add(new Pt(1077.2792f,241.01355f));
D.add(new Pt(1077.4194f,241.56012f));
D.add(new Pt(1077.5762f,241.97432f));
D.add(new Pt(1077.7385f,242.26837f));
D.add(new Pt(1077.9019f,242.48961f));
D.add(new Pt(1078.0355f,242.64545f));
D.add(new Pt(1078.1277f,242.73668f));
D.add(new Pt(1078.2274f,242.7761f));
D.add(new Pt(1078.3519f,242.7907f));
D.add(new Pt(1078.4375f,242.79846f));
D.add(new Pt(1078.4508f,242.74823f));
D.add(new Pt(1078.4443f,242.57587f));
D.add(new Pt(1078.4602f,242.33607f));
D.add(new Pt(1078.4478f,242.13942f));
D.add(new Pt(1078.314f,241.98581f));
D.add(new Pt(1078.0676f,241.7969f));
D.add(new Pt(1077.8152f,241.5553f));
D.add(new Pt(1077.5481f,241.31268f));
D.add(new Pt(1077.1235f,241.11101f));
D.add(new Pt(1076.4846f,240.9375f));
D.add(new Pt(1075.7065f,240.7236f));
D.add(new Pt(1074.8977f,240.40434f));
D.add(new Pt(1074.1907f,240.0023f));
D.add(new Pt(1073.6589f,239.59853f));
D.add(new Pt(1073.1947f,239.22006f));
D.add(new Pt(1072.628f,238.81439f));
D.add(new Pt(1071.9658f,238.35483f));
D.add(new Pt(1071.3447f,237.88823f));
D.add(new Pt(1070.7747f,237.36865f));
D.add(new Pt(1070.1772f,236.64168f));
D.add(new Pt(1069.6401f,235.76544f));
D.add(new Pt(1069.2633f,235.01608f));
D.add(new Pt(1068.9248f,234.49861f));
D.add(new Pt(1068.5313f,234.10236f));
D.add(new Pt(1068.203f,233.72708f));
D.add(new Pt(1068.0194f,233.32863f));
D.add(new Pt(1067.8975f,232.91599f));
D.add(new Pt(1067.7438f,232.5401f));
D.add(new Pt(1067.5193f,232.2116f));
D.add(new Pt(1067.2002f,231.90567f));
D.add(new Pt(1066.7954f,231.62225f));
D.add(new Pt(1066.3665f,231.35892f));
D.add(new Pt(1065.934f,231.01474f));
D.add(new Pt(1065.449f,230.40881f));
D.add(new Pt(1064.9379f,229.44865f));
D.add(new Pt(1064.3981f,228.065f));
D.add(new Pt(1063.5837f,226.09526f));
D.add(new Pt(1062.3154f,223.64513f));
D.add(new Pt(1060.7834f,221.15306f));
D.add(new Pt(1059.1677f,218.73416f));
D.add(new Pt(1057.3105f,216.01971f));
D.add(new Pt(1055.101f,212.8203f));
D.add(new Pt(1052.8223f,209.49297f));
D.add(new Pt(1050.7262f,206.38292f));
D.add(new Pt(1048.7244f,203.33122f));
D.add(new Pt(1046.7927f,200.13571f));
D.add(new Pt(1044.8894f,196.69345f));
D.add(new Pt(1042.6636f,192.68594f));
D.add(new Pt(1040.1063f,188.1657f));
D.add(new Pt(1037.6364f,183.71014f));
D.add(new Pt(1035.2026f,179.52606f));
D.add(new Pt(1032.4388f,175.44589f));
D.add(new Pt(1029.3523f,171.42361f));
D.add(new Pt(1026.105f,167.43378f));
D.add(new Pt(1022.81714f,163.55602f));
D.add(new Pt(1019.79016f,160.13583f));
D.add(new Pt(1017.25867f,157.28741f));
D.add(new Pt(1015.0031f,154.48732f));
D.add(new Pt(1012.6309f,151.16803f));
D.add(new Pt(1010.1212f,147.56247f));
D.add(new Pt(1007.8707f,144.49316f));
D.add(new Pt(1006.1025f,142.255f));
D.add(new Pt(1004.4227f,140.16275f));
D.add(new Pt(1002.3115f,137.5151f));
D.add(new Pt(999.7783f,134.48102f));
D.add(new Pt(997.18164f,131.67596f));
D.add(new Pt(994.6764f,129.32018f));
D.add(new Pt(992.1161f,127.12732f));
D.add(new Pt(989.5199f,124.861725f));
D.add(new Pt(987.2703f,122.61248f));
D.add(new Pt(985.5823f,120.45642f));
D.add(new Pt(984.2371f,118.378555f));
D.add(new Pt(983.00696f,116.549286f));
D.add(new Pt(981.8703f,115.12372f));
D.add(new Pt(980.83545f,113.940445f));
D.add(new Pt(979.9001f,112.84898f));
D.add(new Pt(979.00684f,111.900246f));
D.add(new Pt(978.00085f,111.04654f));
D.add(new Pt(976.9211f,110.21128f));
D.add(new Pt(976.0458f,109.49196f));
D.add(new Pt(975.39484f,108.90872f));
D.add(new Pt(974.62225f,108.309586f));
D.add(new Pt(973.51855f,107.62772f));
D.add(new Pt(972.2818f,106.953224f));
D.add(new Pt(971.24164f,106.390564f));
D.add(new Pt(970.45276f,105.94255f));
D.add(new Pt(969.69763f,105.541245f));
D.add(new Pt(968.876f,105.187836f));
D.add(new Pt(968.0984f,104.9315f));
D.add(new Pt(967.39984f,104.71234f));
D.add(new Pt(966.69415f,104.40988f));
D.add(new Pt(965.9564f,104.01166f));
D.add(new Pt(965.24634f,103.60507f));
D.add(new Pt(964.609f,103.2521f));
D.add(new Pt(963.98926f,102.92521f));
D.add(new Pt(963.2808f,102.56985f));
D.add(new Pt(962.4783f,102.190956f));
D.add(new Pt(961.6415f,101.78747f));
D.add(new Pt(960.7705f,101.29047f));
D.add(new Pt(959.88995f,100.68953f));
D.add(new Pt(959.07355f,100.067535f));
D.add(new Pt(958.3026f,99.47513f));
D.add(new Pt(957.51764f,98.93333f));
D.add(new Pt(956.7043f,98.44908f));
D.add(new Pt(955.8086f,97.97743f));
D.add(new Pt(954.78613f,97.535034f));
D.add(new Pt(953.74255f,97.21652f));
D.add(new Pt(952.85815f,97.01184f));
D.add(new Pt(952.1626f,96.81984f));
D.add(new Pt(951.51526f,96.598724f));
D.add(new Pt(950.85114f,96.3504f));
D.add(new Pt(950.1981f,96.04236f));
D.add(new Pt(949.49677f,95.62599f));
D.add(new Pt(948.7176f,95.08739f));
D.add(new Pt(947.9656f,94.44438f));
D.add(new Pt(947.2908f,93.72169f));
D.add(new Pt(946.6079f,92.91321f));
D.add(new Pt(945.8307f,91.97058f));
D.add(new Pt(944.9707f,90.906815f));
D.add(new Pt(944.0343f,89.81714f));
D.add(new Pt(942.8881f,88.73993f));
D.add(new Pt(941.4706f,87.640366f));
D.add(new Pt(940.00226f,86.45752f));
D.add(new Pt(938.6703f,85.13179f));
D.add(new Pt(937.3889f,83.79176f));
D.add(new Pt(936.08264f,82.74643f));
D.add(new Pt(934.73755f,82.1013f));
D.add(new Pt(933.18176f,81.68915f));
D.add(new Pt(931.2958f,81.380905f));
D.add(new Pt(929.23175f,81.13041f));
D.add(new Pt(927.2445f,80.86415f));
D.add(new Pt(925.5363f,80.53915f));
D.add(new Pt(924.07043f,80.158195f));
D.add(new Pt(922.5502f,79.7347f));
D.add(new Pt(920.907f,79.32344f));
D.add(new Pt(919.44293f,78.951065f));
D.add(new Pt(918.2483f,78.517426f));
D.add(new Pt(917.0083f,77.89395f));
D.add(new Pt(915.5187f,77.062126f));
D.add(new Pt(913.94275f,76.08043f));
D.add(new Pt(912.51666f,75.02583f));
D.add(new Pt(911.3255f,74.030235f));
D.add(new Pt(910.3589f,73.2297f));
D.add(new Pt(909.5635f,72.62419f));
D.add(new Pt(908.844f,72.09895f));
D.add(new Pt(908.10156f,71.562035f));
D.add(new Pt(907.3176f,70.990295f));
D.add(new Pt(906.57043f,70.40341f));
D.add(new Pt(905.90393f,69.845345f));
D.add(new Pt(905.28357f,69.373795f));
D.add(new Pt(904.72784f,69.03668f));
D.add(new Pt(904.3162f,68.83226f));
D.add(new Pt(904.0328f,68.68068f));
D.add(new Pt(903.7326f,68.476974f));
D.add(new Pt(903.2993f,68.206406f));
D.add(new Pt(902.7999f,67.9574f));
D.add(new Pt(902.4162f,67.80856f));
D.add(new Pt(902.2035f,67.75993f));
D.add(new Pt(902.03784f,67.7654f));
D.add(new Pt(901.8456f,67.78202f));
D.add(new Pt(901.68823f,67.77494f));
D.add(new Pt(901.5847f,67.71831f));
D.add(new Pt(901.47064f,67.6333f));
D.add(new Pt(901.3453f,67.57741f));
D.add(new Pt(901.25714f,67.5686f));
D.add(new Pt(901.1741f,67.57628f));
D.add(new Pt(901.0398f,67.57887f));
D.add(new Pt(900.88293f,67.57825f));
D.add(new Pt(900.74457f,67.578064f));
D.add(new Pt(900.6035f,67.57824f));
D.add(new Pt(900.43787f,67.57793f));
D.add(new Pt(900.2733f,67.57583f));
D.add(new Pt(900.1275f,67.58105f));
D.add(new Pt(899.96155f,67.60822f));
D.add(new Pt(899.7709f,67.60524f));
D.add(new Pt(899.6612f,67.43388f));
D.add(new Pt(899.6875f,67.06104f));

	}

	public void displace() {
		calcDisplacement();
		for(Pt p: A){
			p.add(displacement);
		}
		for(Pt p: B){
			p.add(displacement);
		}
		for(Pt p: C){
			p.add(displacement);
		}
		for(Pt p: D){
			p.add(displacement);
		}
		//displacement = new Pt(0,0);
		
	}

	public void calcDisplacement() {
		displacement= new Pt(currentTouch.x-prevTouch.x,currentTouch.y-prevTouch.y);		
	}
	
	public String toString(){
		return name;
	}

	public void setTo(MotionPath mp) {
		for(Pt p: mp.A){
			A.add(p);
		}
		for(Pt p: mp.B){
			B.add(p);
		}
		for(Pt p: mp.C){
			C.add(p);
		}
		for(Pt p: mp.D){
			D.add(p);
		}
	}

	public void setTo(MultiTouchController mController) {
		A = mController.getHistoryOf(0);
		B = mController.getHistoryOf(1);
		C = mController.getHistoryOf(2);
		D = mController.getHistoryOf(3);
		ellapsed_time = mController.getElapsedTime();
		
	}
}

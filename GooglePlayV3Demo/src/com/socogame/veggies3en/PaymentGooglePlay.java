package com.socogame.veggies3en;

import com.soco.android.google.util.IabHelper;
import com.soco.android.google.util.IabHelper.QueryInventoryFinishedListener;
import com.soco.android.google.util.IabResult;
import com.soco.android.google.util.Inventory;
import com.soco.android.google.util.Purchase;

import android.app.Activity;
import android.content.Intent;

public class PaymentGooglePlay {
	String base64EncodedPublicKey = "";
	private Activity mActivity;
	
 	private static final int RC_REQUEST = 10001;
 	// The helper object
    private IabHelper mHelper;
	double Price;
    private boolean iap_is_ok = false;
	public PaymentGooglePlay(Activity activity) {
		mActivity = activity;
		mHelper = new IabHelper(mActivity, base64EncodedPublicKey);
        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                System.out.println(">>> Setup finished.");
                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                	System.out.println(">>> Problem setting up in-app billing: " + result);
                    return;
                }
                iap_is_ok = true;
                // Hooray, IAB is fully set up. Now, let's get an inventory of stuff we own.
                System.out.println(">>> Setup successful. Querying inventory.");
            }
        });
        System.out.println(">>> init");
	}
	String info;
	public void pay(String id, final String order) {
		if (iap_is_ok) {
			String skus = "com.socogame.veggies3en.600gem";
			Price = 0.0;
			long time = System.currentTimeMillis();
			info = "" + time;
			System.out.println(">>> 验证信息：" + info);
			
			mHelper.launchPurchaseFlow(mActivity, skus, RC_REQUEST, new IabHelper.OnIabPurchaseFinishedListener() {
		        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
		        	 System.out.println("Purchase finished: " + result + ", purchase: " + purchase);
		            if (result.isFailure()) {
		                // Oh noes!
		                System.out.println(">>> Error purchasing: " + result);
		                return;
		            }

		           if (purchase.getSku().equals("com.socogame.veggies3en.600gem")) {
		            	mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
		                    public void onConsumeFinished(Purchase purchase, IabResult result) {
		                    	System.out.println(">>> Consumption finished. Purchase: " + purchase + ", result: " + result);

		                        // We know this is the "gas" sku because it's the only one we consume,
		                        // so we don't check which sku was consumed. If you have more than one
		                        // sku, you probably should check...
		                        if (result.isSuccess()) {
		                        	 String payload = purchase.getDeveloperPayload();  
		                        	 System.out.println(">>> 获取验证信息:" + payload);
		                        	 if (info.equals(payload)) {
		                        		 System.out.println(">>> 验证成功");
		                        	 } else {
		                        		 System.out.println(">>> 验证失败");
		                        	 }
		                            // successfully consumed, so we apply the effects of the item in our
		                            // game world's logic, which in our case means filling the gas tank a bit
		                        	if (purchase.getSku().equals("com.socogame.veggies3en.600gem")){
		                        		System.out.println(">>> 支付成功");
		                			} else {
		                			}
		                        }
		                        else {
		                        	System.out.println(">>> Error while consuming: " + result);
		                        }
		                    }
		                });
					}
		            
		        }
		    }, info);
		} else {
			System.out.println(">>> Billing not initialized.");
		}
	}
	
	public void query() {
		purchase_old = null;
		mHelper.queryInventoryAsync(new QueryInventoryFinishedListener() {
			public void onQueryInventoryFinished(IabResult result, Inventory inv) {
				System.out.println(">>> query result = " + result.getMessage());
				purchase_old = inv.getPurchase("com.socogame.veggies3en.600gem");
				if (purchase_old != null) {
					System.out.println(">>> query result : purchase_old = " + purchase_old.getDeveloperPayload());
				} else {
					System.out.println(">>> query result : purchase_old = null");
				}
			}
		});
	}
	
	public void consume() {
		if (purchase_old.getSku().equals("com.socogame.veggies3en.600gem")) {
        	mHelper.consumeAsync(purchase_old, new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                	System.out.println(">>> consume finished. Purchase: " + purchase + ", result: " + result);
                	info = "";
                    if (result.isSuccess()) {
                    	 String payload = purchase.getDeveloperPayload();  
                    	 System.out.println(">>> 获取验证信息:" + payload);
                    	 if (info.equals(payload)) {
                    		 System.out.println(">>> 验证成功");
                    	 } else {
                    		 System.out.println(">>> 验证失败");
                    	 }
                        // successfully consumed, so we apply the effects of the item in our
                        // game world's logic, which in our case means filling the gas tank a bit
                    	if (purchase.getSku().equals("com.socogame.veggies3en.600gem")){
                    		System.out.println(">>> 支付成功");
            			} else {
            			}
                    }
                    else {
                    	System.out.println(">>> Error while consuming: " + result);
                    }
                }
            });
		}
	}
	
	Purchase purchase_old;
	
	public void onDestroy() {
		if (mHelper != null) mHelper.dispose();
    		mHelper = null;
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println(">>> onActivityResult(" + requestCode + "," + resultCode + "," + data);
		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			// super.onActivityResult(requestCode, resultCode, data);
		} else {
			System.out.println(">>> onActivityResult handled by IABUtil.");
		}
    }
}

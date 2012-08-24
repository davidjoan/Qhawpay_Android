package pe.qhawpay.android;

import android.app.Activity;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.os.Bundle;

public class ShortCutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);

        Intent intent = new Intent();

        Intent launchIntent = new Intent(this,MainActivity.class);

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Qhawpay");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        setResult(RESULT_OK, intent);		
	}
}

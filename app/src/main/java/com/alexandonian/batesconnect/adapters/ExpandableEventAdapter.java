package com.alexandonian.batesconnect.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alexandonian.batesconnect.R;
import com.alexandonian.batesconnect.expandingEvents.ExpandingLayout;
import com.alexandonian.batesconnect.infoItems.EventItem;

import java.util.List;

/**
 * This is a custom array adapter used to populate the listview whose items will
 * expand to display extra content in addition to the default display.
 */
public class ExpandableEventAdapter extends ArrayAdapter<EventItem> {

    private List<EventItem> mData;
    private int mLayoutViewResourceId;

    public ExpandableEventAdapter(Context context, int layoutViewResourceId,
                                  List<EventItem> data) {
        super(context, layoutViewResourceId, data);
        mData = data;
        mLayoutViewResourceId = layoutViewResourceId;
    }

    /**
     * Populates the item in the listview cell with the appropriate data. This method
     * sets the thumbnail image, the title and the extra text. This method also updates
     * the layout parameters of the item's view so that the image and title are centered
     * in the bounds of the collapsed view, and such that the extra text is not displayed
     * in the collapsed state of the cell.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final EventItem object = mData.get(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(mLayoutViewResourceId, parent, false);
        }

        LinearLayout linearLayout = (LinearLayout) (convertView.findViewById(
                R.id.item_linear_layout));
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, object.getCollapsedHeight());
        linearLayout.setLayoutParams(linearLayoutParams);

        ImageView imgView = (ImageView) convertView.findViewById(R.id.image_view);
        TextView titleView = (TextView) convertView.findViewById(R.id.title_view);
        TextView dateView = (TextView) convertView.findViewById(R.id.date_view);
        TextView textView = (TextView) convertView.findViewById(R.id.text_view);

        titleView.setText(object.getTitle());
        dateView.setText(object.getDate());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Drawable img = getContext().getResources().getDrawable(object.getImgResource(), null);
            imgView.setImageDrawable(img);
        } else {
            imgView.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), object
                    .getImgResource(), null));
        }
//        imgView.setImageBitmap(getCroppedBitmap(BitmapFactory.decodeResource(getContext()
//                .getResources(), object.getImgResource(), null)));
        textView.setText(object.getDescription());

        convertView.setLayoutParams(new ListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT));

        ExpandingLayout expandingLayout = (ExpandingLayout) convertView.findViewById(R.id
                .expanding_layout);
        expandingLayout.setExpandedHeight(object.getExpandedHeight());
        expandingLayout.setSizeChangedListener(object);

        if (!object.isExpanded()) {
            expandingLayout.setVisibility(View.GONE);
        } else {
            expandingLayout.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    /**
     * Crops a circle out of the thumbnail photo.
     */
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Config.ARGB_8888);

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        int halfWidth = bitmap.getWidth() / 2;
        int halfHeight = bitmap.getHeight() / 2;

        canvas.drawCircle(halfWidth, halfHeight, Math.max(halfWidth, halfHeight), paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


}
